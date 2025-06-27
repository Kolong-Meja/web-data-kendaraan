package com.web_data_kendaraan.api_v1.dtos;

import com.web_data_kendaraan.api_v1.models.Vehicle;

import jakarta.persistence.Tuple;

public record VehicleDTO(
    String nrk,
    String namaPemilik,
    String alamat,
    String merkKendaraan,
    Integer tahunPembuatan,
    Integer kapSilinder,
    String warnaKendaraan,
    String bahanBakar) {
  public VehicleDTO(
      String nrk,
      String namaPemilik,
      String alamat,
      String merkKendaraan,
      Integer tahunPembuatan,
      Integer kapSilinder,
      String warnaKendaraan,
      String bahanBakar) {
    this.nrk = nrk;
    this.namaPemilik = namaPemilik;
    this.alamat = alamat;
    this.merkKendaraan = merkKendaraan;
    this.tahunPembuatan = tahunPembuatan;
    this.kapSilinder = kapSilinder;
    this.warnaKendaraan = warnaKendaraan;
    this.bahanBakar = bahanBakar;
  }

  public static final VehicleDTO fromObject(Vehicle vehicle) {
    return new VehicleDTO(
        vehicle.getNrk(),
        vehicle.getNamaPemilik(),
        vehicle.getAlamat(),
        vehicle.getMerkKendaraan(),
        vehicle.getTahunPembuatan(),
        vehicle.getKapSilinder(),
        vehicle.getWarnaKendaraan(),
        vehicle.getBahanBakar());
  }

  public static final VehicleDTO fromTuple(Tuple tuple) {
    return new VehicleDTO(
      tuple.get("nrk", String.class),
      tuple.get("namaPemilik", String.class),
      tuple.get("alamat", String.class),
      tuple.get("merkKendaraan", String.class),
      tuple.get("tahunPembuatan", Integer.class),
      tuple.get("kapSilinder", Integer.class),
      tuple.get("warnaKendaraan", String.class),
      tuple.get("bahanBakar", String.class)
    );
  }
}
