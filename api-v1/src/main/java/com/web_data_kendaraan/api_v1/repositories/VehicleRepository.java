package com.web_data_kendaraan.api_v1.repositories;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import com.web_data_kendaraan.api_v1.dtos.VehicleDTO;
import com.web_data_kendaraan.api_v1.exceptions.ResourceNotFoundException;
import com.web_data_kendaraan.api_v1.interfaces.IVehicle;
import com.web_data_kendaraan.api_v1.models.Vehicle;
import com.web_data_kendaraan.api_v1.payloads.requests.CreateVehicleRequest;
import com.web_data_kendaraan.api_v1.payloads.requests.SearchRequest;
import com.web_data_kendaraan.api_v1.payloads.requests.UpdateVehicleRequest;
import com.web_data_kendaraan.api_v1.payloads.responses.ApiResponse;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

@Repository
public class VehicleRepository implements IVehicle {
  private final EntityManager entityManager;
  private static final Logger LOG = LoggerFactory.getLogger(VehicleRepository.class);

  public VehicleRepository(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  private final void searchRequestPredicate(
      SearchRequest request,
      List<Predicate> targetPredicates,
      CriteriaBuilder targetCriteriaBuilder,
      Root<Vehicle> targetRoot,
      List<String> fields) {
    String queryLower = "%" + request.query().toLowerCase() + "%";

    var likePredicates = fields.stream()
        .map(field -> targetCriteriaBuilder.like(targetCriteriaBuilder.lower(targetRoot.get(field)), queryLower))
        .toList();

    if (!likePredicates.isEmpty()) {
      targetPredicates.add(targetCriteriaBuilder.or(likePredicates.toArray(Predicate[]::new)));
    }
  }

  @Override
  public ResponseEntity<ApiResponse> findAll(SearchRequest searchRequest) {
    try {
      CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
      CriteriaQuery<Tuple> selectQuery = criteriaBuilder.createQuery(Tuple.class);
      List<Predicate> predicates = new ArrayList<>();
      Root<Vehicle> baseRoot = selectQuery.from(Vehicle.class);

      /**
       * CASE WHEN LIKE QUERY USED.
       */
      searchRequestPredicate(
          searchRequest,
          predicates,
          criteriaBuilder,
          baseRoot,
          Arrays.asList("nrk", "namaPemilik", "merkKendaraan"));

      selectQuery.multiselect(
          baseRoot.get("nrk").alias("nrk"),
          baseRoot.get("namaPemilik").alias("namaPemilik"),
          baseRoot.get("alamat").alias("alamat"),
          baseRoot.get("merkKendaraan").alias("merkKendaraan"),
          baseRoot.get("tahunPembuatan").alias("tahunPembuatan"),
          baseRoot.get("kapSilinder").alias("kapSilinder"),
          baseRoot.get("warnaKendaraan").alias("warnaKendaraan"),
          baseRoot.get("bahanBakar").alias("bahanBakar")).distinct(true)
          .where(criteriaBuilder.and(predicates.toArray(Predicate[]::new)));

      TypedQuery<Tuple> typedQuery = entityManager.createQuery(selectQuery);

      List<Tuple> result = typedQuery.getResultList();
      List<VehicleDTO> resource = result.stream()
          .map(VehicleDTO::fromTuple)
          .toList();

      var now = LocalDateTime.now(ZoneId.of("Asia/Jakarta"));
      var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
      ApiResponse response = new ApiResponse(
          HttpStatus.OK.value(),
          true,
          "Succesfully fetch vehicles ✅",
          now.format(formatter),
          resource);

      LOG.info(response.message());

      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (NoResultException e) {
      LOG.warn("No vehicles found", e);
      throw new ResourceNotFoundException(e.getMessage());
    }
  }

  @Override
  public ResponseEntity<ApiResponse> findOne(String nrk) {
    try {
      CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
      CriteriaQuery<Tuple> selectQuery = criteriaBuilder.createQuery(Tuple.class);
      Root<Vehicle> baseSelectRoot = selectQuery.from(Vehicle.class);

      selectQuery.multiselect(
          baseSelectRoot.get("nrk").alias("nrk"),
          baseSelectRoot.get("namaPemilik").alias("namaPemilik"),
          baseSelectRoot.get("alamat").alias("alamat"),
          baseSelectRoot.get("merkKendaraan").alias("merkKendaraan"),
          baseSelectRoot.get("tahunPembuatan").alias("tahunPembuatan"),
          baseSelectRoot.get("kapSilinder").alias("kapSilinder"),
          baseSelectRoot.get("warnaKendaraan").alias("warnaKendaraan"),
          baseSelectRoot.get("bahanBakar").alias("bahanBakar")).distinct(true)
          .where(criteriaBuilder.equal(baseSelectRoot.get("nrk"), nrk));

      TypedQuery<Tuple> typedQuery = entityManager.createQuery(selectQuery);
      var resource = VehicleDTO.fromTuple(typedQuery.getSingleResult());

      var now = LocalDateTime.now(ZoneId.of("Asia/Jakarta"));
      var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
      ApiResponse response = new ApiResponse(HttpStatus.OK.value(), true,
          String.format("Successfully fetch vehicle with nrk %s ✅", nrk),
          now.format(formatter),
          resource);

      LOG.info(response.message());

      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (NoResultException e) {
      throw new ResourceNotFoundException(e.getMessage());
    }
  }

  @Override
  @Transactional
  public ResponseEntity<ApiResponse> save(CreateVehicleRequest createVehicleRequest) {
    Vehicle vehicle = new Vehicle();
    vehicle.setNrk(createVehicleRequest.nrk());
    vehicle.setNamaPemilik(createVehicleRequest.namaPemilik());
    vehicle.setAlamat(createVehicleRequest.alamat());
    vehicle.setMerkKendaraan(createVehicleRequest.merkKendaraan());
    vehicle.setTahunPembuatan(createVehicleRequest.tahunPembuatan());
    vehicle.setKapSilinder(createVehicleRequest.kapSilinder());
    vehicle.setWarnaKendaraan(createVehicleRequest.warnaKendaraan());
    vehicle.setBahanBakar(createVehicleRequest.bahanBakar());

    entityManager.persist(vehicle);
    entityManager.flush();

    var now = LocalDateTime.now(ZoneId.of("Asia/Jakarta"));
    var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    ApiResponse response = new ApiResponse(HttpStatus.OK.value(), true, "Successfully add new vehicle ✅",
        now.format(formatter),
        vehicle);

    LOG.info(response.message());

    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  @Override
  @Transactional
  public ResponseEntity<ApiResponse> update(String nrk, UpdateVehicleRequest updateVehicleRequest) {
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaUpdate<Vehicle> criteriaUpdate = criteriaBuilder.createCriteriaUpdate(Vehicle.class);
    Root<Vehicle> baseSelectRoot = criteriaUpdate.from(Vehicle.class);

    criteriaUpdate.set("nrk", updateVehicleRequest.nrk());
    criteriaUpdate.set("namaPemilik", updateVehicleRequest.namaPemilik());
    criteriaUpdate.set("alamat", updateVehicleRequest.alamat());
    criteriaUpdate.set("merkKendaraan", updateVehicleRequest.merkKendaraan());
    criteriaUpdate.set("tahunPembuatan", updateVehicleRequest.tahunPembuatan());
    criteriaUpdate.set("kapSilinder", updateVehicleRequest.kapSilinder());
    criteriaUpdate.set("warnaKendaraan", updateVehicleRequest.warnaKendaraan());
    criteriaUpdate.set("bahanBakar", updateVehicleRequest.bahanBakar());

    criteriaUpdate.where(criteriaBuilder.equal(baseSelectRoot.get("nrk"), nrk));

    int updated = entityManager.createQuery(criteriaUpdate).executeUpdate();
    if (updated != 1) {
      throw new ResourceNotFoundException(String.format("Vehicle with nrk %s not found ❌", nrk));
    }

    entityManager.flush();
    entityManager.clear();

    var now = LocalDateTime.now(ZoneId.of("Asia/Jakarta"));
    var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    ApiResponse response = new ApiResponse(HttpStatus.OK.value(), true,
        String.format("Successfully update Vehicle with nrk %s ✅", nrk),
        now.format(formatter), new HashMap<>());

    LOG.info(response.message());

    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @Override
  @Transactional
  public ResponseEntity<ApiResponse> delete(String nrk) {
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaDelete<Vehicle> criteriaDelete = criteriaBuilder.createCriteriaDelete(Vehicle.class);
    Root<Vehicle> baseDeleteRoot = criteriaDelete.from(Vehicle.class);

    criteriaDelete.where(criteriaBuilder.equal(baseDeleteRoot.get("nrk"), nrk));

    int deletedCount = entityManager.createQuery(criteriaDelete).executeUpdate();
    if (deletedCount == 0) {
      throw new ResourceNotFoundException(String.format("Vehicle with nrk %s not found ❌", nrk));
    }

    entityManager.flush();
    entityManager.clear();

    var now = LocalDateTime.now(ZoneId.of("Asia/Jakarta"));
    var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    ApiResponse response = new ApiResponse(HttpStatus.OK.value(), true,
        String.format("Successfully delete Vehicle with nrk %s ✅", nrk),
        now.format(formatter), new HashMap<>());

    LOG.info(response.message());

    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
