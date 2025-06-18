package com.web_data_pribadi.api_v1.seeders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.web_data_pribadi.api_v1.models.Vehicle;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

@Component
@Transactional
public class VehicleSeeder implements CommandLineRunner {
  private final EntityManager entityManager;
  private static final Logger LOG = LoggerFactory.getLogger(VehicleSeeder.class);

  public VehicleSeeder(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Override
  public void run(String... args) throws Exception {
    loadVehicleData();
  }

  private final void loadVehicleData() {
    LOG.info("Running Vehicle Seeder...");

    try {
      CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

      CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
      Root<Vehicle> baseRoleRoot = countQuery.from(Vehicle.class);

      countQuery.select(criteriaBuilder.countDistinct(baseRoleRoot));
      Long totalElements = entityManager.createQuery(countQuery).getSingleResult();

      if (totalElements == 0) {
        /**
         * DATA PERTAMA.
         */
        Vehicle firstVehicle = new Vehicle();
        firstVehicle.setNrk("B-7424-JHD");
        firstVehicle.setNamaPemilik("John Doe");
        firstVehicle.setAlamat("Jl. Cemara no. 1");
        firstVehicle.setMerkKendaraan("Honda Beat");
        firstVehicle.setTahunPembuatan(2017);
        firstVehicle.setKapSilinder(125);
        firstVehicle.setWarnaKendaraan("Hitam");
        firstVehicle.setBahanBakar("Bensin");

        /**
         * DATA KEDUA.
         */
        Vehicle secondVehicle = new Vehicle();
        secondVehicle.setNrk("B-7425-JHS");
        secondVehicle.setNamaPemilik("John Smith");
        secondVehicle.setAlamat("Jl. Cemara no. 2");
        secondVehicle.setMerkKendaraan("Honda Vario");
        secondVehicle.setTahunPembuatan(2020);
        secondVehicle.setKapSilinder(150);
        secondVehicle.setWarnaKendaraan("Merah");
        secondVehicle.setBahanBakar("Bensin");

        entityManager.persist(firstVehicle);
        entityManager.persist(secondVehicle);
        entityManager.flush();
      } else {
        LOG.info("Skipping vehicle seeder, {} records already exist", totalElements);
      }
    } catch (Exception e) {
      LOG.error("Error during vehicle seeding: {}", e.getMessage(), e);
    }
  }
}
