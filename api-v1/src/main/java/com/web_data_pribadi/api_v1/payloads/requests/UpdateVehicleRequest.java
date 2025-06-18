package com.web_data_pribadi.api_v1.payloads.requests;

import java.util.Objects;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateVehicleRequest(
    String nrk,
    String namaPemilik,
    String alamat,
    String merkKendaraan,
    Integer tahunPembuatan,
    Integer kapSilinder,
    String warnaKendaraan,
    String bahanBakar) {
  public UpdateVehicleRequest(
      @Size(max = 10) @NotBlank(message = "Nomor Registrasi Kendaraan cannot be blank.") String nrk,

      @Size(max = 100) @NotBlank(message = "Nama Pemilik cannot be blank.") String namaPemilik,

      @NotNull(message = "Alamat cannot be null.") String alamat,

      @NotNull(message = "Merk Kendaraan cannot be null.") @Size(max = 100) String merkKendaraan,

      @Size(max = 4) @Min(value = 1886, message = "Minimum year of manufacture is 1886") @Max(value = 2100, message = "The year of manufacture cannot be later than 2100") Integer tahunPembuatan,

      @Min(value = 50, message = "Kapasitas Silinder minimal 1886") @Max(value = 8000, message = "Kapasitas Silinder tidak boleh di atas 2100") Integer kapSilinder,

      @NotNull(message = "Warna Kendaraan cannot be null.") @Size(max = 20) String warnaKendaraan,

      @NotNull(message = "Bahan Bakar cannot be null.") @Size(max = 20) String bahanBakar) {
    this.nrk = Objects.requireNonNull(nrk, "Nomor Registrasi Kendaraan cannot be null.");
    this.namaPemilik = Objects.requireNonNull(namaPemilik, "Nama Pemilik cannot be null.");
    this.alamat = Objects.requireNonNull(alamat, "Alamat cannot be null.");
    this.merkKendaraan = Objects.requireNonNull(merkKendaraan, "Merk Kendaraan cannot be null.");
    this.tahunPembuatan = Objects.requireNonNull(tahunPembuatan, "Tahun Pembuatan cannot be null.");
    this.kapSilinder = Objects.requireNonNull(kapSilinder, "Kapasitas Silinder cannot be null.");
    this.warnaKendaraan = Objects.requireNonNull(warnaKendaraan, "Warna Kendaraan cannot be null.");
    this.bahanBakar = Objects.requireNonNull(bahanBakar, "Bahan Bakar cannot be null.");
  }
}
