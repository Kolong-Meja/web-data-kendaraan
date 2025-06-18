create table vehicles (
  nrk varchar(10) primary key not null,
  nama_pemilik varchar(100) unique not null,
  alamat text null,
  merk_kendaraan varchar(100) null,
  tahun_pembuatan integer check (tahun_pembuatan >= 1886 AND tahun_pembuatan <= 2100) null,
  kap_silinder integer check (kap_silinder >= 50 AND kap_silinder <= 8000) null,
  warna_kendaraan varchar(20) null,
  bahan_bakar varchar(20) null
);

comment on column vehicles.nrk is 'Nomor Registrasi Kendaraan.';
comment on column vehicles.kap_silinder is 'Kapasitas silinder mesin dalam cc';

create index nama_pemilik_idx on vehicles(nama_pemilik);
create index merk_kendaraan_idx on vehicles(merk_kendaraan);
create index tahun_pembuatan_idx on vehicles(tahun_pembuatan);
create index warna_kendaraan_idx on vehicles(warna_kendaraan);