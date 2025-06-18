package com.web_data_pribadi.api_v1.models;

import java.util.Objects;

import org.hibernate.annotations.Comment;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "vehicles", indexes = {
    @Index(name = "nama_pemilik_idx", columnList = "nama_pemilik"),
    @Index(name = "merk_kendaraan_idx", columnList = "merk_kendaraan"),
    @Index(name = "tahun_pembuatan_idx", columnList = "tahun_pembuatan"),
    @Index(name = "warna_kendaraan_idx", columnList = "warna_kendaraan")
})
public class Vehicle {
  @Id
  @Size(max = 10)
  @NotBlank(message = "Nomor Registrasi Kendaraan cannot be blank.")
  @Column(name = "nrk", length = 10, nullable = false)
  @Comment(value = "Nomor Registrasi Kendaraan.")
  private String nrk;

  @Size(max = 100)
  @NotBlank(message = "Nama Pemilik cannot be blank.")
  @Column(name = "nama_pemilik", length = 100, nullable = false)
  private String namaPemilik;

  @Column(name = "alamat", columnDefinition = "TEXT", nullable = true)
  private String alamat;

  @Size(max = 100)
  @Column(name = "merk_kendaraan", length = 100, nullable = true)
  private String merkKendaraan;

  @Min(value = 1886, message = "Minimum year of manufacture is 1886")
  @Max(value = 2100, message = "The year of manufacture cannot be later than 2100")
  @Column(name = "tahun_pembuatan", columnDefinition = "INTEGER", nullable = true)
  private Integer tahunPembuatan;

  @Min(value = 50, message = "Kapasitas Silinder minimal 50")
  @Max(value = 8000, message = "Kapasitas Silinder tidak boleh di atas 8000")
  @Column(name = "kap_silinder", columnDefinition = "INTEGER", nullable = true)
  @Comment("Kapasitas silinder mesin dalam cc")
  private Integer kapSilinder;

  @Size(max = 20)
  @Column(name = "warna_kendaraan", length = 20, nullable = true)
  private String warnaKendaraan;

  @Size(max = 20)
  @Column(name = "bahan_bakar", length = 20, nullable = true)
  private String bahanBakar;

  public Vehicle(
      String nrk,
      String namaPemilik,
      String alamat,
      String merkKendaraan,
      Integer tahunPembuatan,
      Integer kapSilinder,
      String warnaKendaraan,
      String bahanBakar) {
    this.nrk = Objects.requireNonNull(nrk, "Nomor Registrasi Kendaraan cannot be null.");
    this.namaPemilik = Objects.requireNonNull(namaPemilik, "Nama Pemilik cannot be null.");
    this.alamat = Objects.requireNonNull(alamat, "Alamat cannot be null.");
    this.merkKendaraan = Objects.requireNonNull(merkKendaraan, "Merk Kendaraan cannot be null.");
    this.tahunPembuatan = Objects.requireNonNull(tahunPembuatan, "Tahun Pembuatan cannot be null.");
    this.kapSilinder = Objects.requireNonNull(kapSilinder, "Kapasitas Silinder cannot be null.");
    this.warnaKendaraan = Objects.requireNonNull(warnaKendaraan, "Warna Kendaraan cannot be null.");
    this.bahanBakar = Objects.requireNonNull(bahanBakar, "Bahan Bakar cannot be null.");
  }

  public Vehicle() {
  }

  // GETTERS
  public String getNrk() {
    return this.nrk;
  }

  public String getNamaPemilik() {
    return this.namaPemilik;
  }

  public String getAlamat() {
    return this.alamat;
  }

  public String getMerkKendaraan() {
    return this.merkKendaraan;
  }

  public Integer getTahunPembuatan() {
    return this.tahunPembuatan;
  }

  public Integer getKapSilinder() {
    return this.kapSilinder;
  }

  public String getWarnaKendaraan() {
    return this.warnaKendaraan;
  }

  public String getBahanBakar() {
    return this.bahanBakar;
  }

  // SETTERS
  public void setNrk(String value) {
    this.nrk = value;
  }

  public void setNamaPemilik(String value) {
    this.namaPemilik = value;
  }

  public void setAlamat(String value) {
    this.alamat = value;
  }

  public void setMerkKendaraan(String value) {
    this.merkKendaraan = value;
  }

  public void setTahunPembuatan(Integer value) {
    this.tahunPembuatan = value;
  }

  public void setKapSilinder(Integer value) {
    this.kapSilinder = value;
  }

  public void setWarnaKendaraan(String value) {
    this.warnaKendaraan = value;
  }

  public void setBahanBakar(String value) {
    this.bahanBakar = value;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null || getClass() != obj.getClass())
      return false;

    Vehicle vehicle = (Vehicle) obj;
    return Objects.equals(nrk, vehicle.nrk) &&
        Objects.equals(namaPemilik, vehicle.namaPemilik) &&
        Objects.equals(alamat, vehicle.alamat) &&
        Objects.equals(merkKendaraan, vehicle.merkKendaraan) &&
        Objects.equals(tahunPembuatan, vehicle.tahunPembuatan) &&
        Objects.equals(kapSilinder, vehicle.kapSilinder) &&
        Objects.equals(warnaKendaraan, vehicle.warnaKendaraan) &&
        Objects.equals(bahanBakar, vehicle.bahanBakar);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        nrk,
        namaPemilik,
        alamat,
        merkKendaraan,
        tahunPembuatan,
        kapSilinder,
        warnaKendaraan,
        bahanBakar);
  }

  @Override
  public String toString() {
    return "Vehicle{" +
        "nrk='" + nrk + '\'' +
        ", namaPemilik='" + namaPemilik + '\'' +
        ", alamat='" + alamat + '\'' +
        ", merkKendaraan='" + merkKendaraan + '\'' +
        ", tahunPembuatan=" + tahunPembuatan +
        ", kapSilinder=" + kapSilinder +
        ", warnaKendaraan='" + warnaKendaraan + '\'' +
        ", bahanBakar='" + bahanBakar + '\'' +
        '}';
  }
}
