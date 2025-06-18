# Web Data Kendaraan

A full-stack web application for managing vehicle data, consisting of:

- Backend: Spring Boot REST API (api-v1)
- Frontend: Vite + Tailwind CSS + Vanilla JavaScript (frontend)
- Database: PostgreSQL + Flyway migrations
- Admin UI: pgAdmin4
- Containerization: Docker Compose & Docker Bake

## Fitur Utama
- CRUD kendaraan (Create, Read, Update, Delete) melalui REST API
- Pencarian (live search) dengan debounce untuk filter data tanpa reload
- UI responsif dengan Tailwind CSS
- Otomatisasi migrasi skema dengan Flyway
- Manajemen database via pgAdmin4

## Struktur Folder
```bash
.
├── .env               # Konfigurasi environment variables
├── .env.example       # Contoh template environment variables
├── compose.yml        # Docker Compose configuration
├── docker-bake.hcl    # Docker Bake targets
├── servers.json       # pgAdmin server definitions
├── README.md          # Dokumentasi ini
├── api-v1             # Spring Boot API
│   ├── src
│   └── Dockerfile
├── db
│   ├── postgres       # Volume mount data PostgreSQL
│   └── pgadmin        # Volume mount pgAdmin data
└── frontend           # Frontend Vite + Tailwind
    ├── src
    ├── public
    └── Dockerfile
```

## Prasyarat
- Docker & Docker Compose
- (Opsional) Java 17+ & Maven (untuk build lokal API)
- (Opsional) Node.js 18+ & npm/yarn (untuk dev mode frontend)

## Konfigurasi Environment
Salin dan sesuaikan file .env.example menjadi .env di root folder:

```bash
# .env
POSTGRES_USER=postgres
POSTGRES_PASSWORD=password
POSTGRES_DB=some_db
POSTGRES_PORT=5432
POSTGRES_TZ=Asia/Jakarta

PGADMIN_PORT=8081
PGADMIN_DEFAULT_EMAIL=admin@example.com
PGADMIN_DEFAULT_PASSWORD=admin

VITE_API_URL=http://localhost:8082/api/v1/vehicles
```

## Cara Build & Running

### 1. Build Images dengan Docker Bake

```bash
docker buildx bake default
```

Ini akan membangun:

- datakendaraan-api:latest dari api-v1/Dockerfile
- datakendaraan-frontend:latest dari frontend/Dockerfile

### 2. Jalankan Docker Compose

```bash
docker-compose -f compose.yml up -d
```

Layanan yang tersedia:

- PostgreSQL
  - Host: localhost
  - Port: ${POSTGRES_PORT} (default 5432)
- pgAdmin4
  - URL: http://localhost:${PGADMIN_PORT} (default 8081)
  - Login: PGADMIN_DEFAULT_EMAIL / PGADMIN_DEFAULT_PASSWORD
  - Konfigurasi server otomatis di-mount dari servers.json
- Spring Boot API
  - URL Base: http://localhost:8082/api
  - Endpoint Kendaraan:
  - GET /api/v1/vehicles
  - GET /api/v1/vehicles/{nrk}
  - POST /api/v1/vehicles
  - PUT /api/v1/vehicles/{nrk}
  - DELETE /api/v1/vehicles/{nrk}
- Frontend
  - URL: http://localhost:3000

## Pengembangan Lokal

### Backend (Spring Boot)

```bash
cd api-v1
./mvnw spring-boot:run
```

API akan berjalan di http://localhost:8080/api.

### Frontend (Vite)

```bash
cd frontend
npm install
npm run dev
```

Frontend akan berjalan di http://localhost:3000.

> Pastikan environment variable VITE_API_URL di-export atau di-.env menunjuk ke http://localhost:8082/api/v1/vehicles

## Database Migration (Flyway)

Semua skrip migrasi SQL disimpan di **api-v1/src/main/resources/db/migration**.

Flyway otomatis berjalan saat aplikasi Spring Boot start.

## CORS

Pada VehicleController.java, CORS diizinkan untuk:

```java
@CrossOrigin(
  origins = {"http://localhost:3000","http://127.0.0.1:3000"},
  allowCredentials = "true",
  methods = {GET, POST, PUT, PATCH, DELETE}
)
```

Jika front-end Anda dijalankan pada host berbeda, tambahkan origin tersebut di anotasi.

## Penggunaan API
- ### Fetch semua kendaraan
  - GET /api/v1/vehicles?q={query}
- ### Fetch satu kendaraan
  - GET /api/v1/vehicles/{nrk}
- ### Create kendaraan
  - POST /api/v1/vehicles
  ```json
  {
    "nrk": "B-1234-XY",
    "namaPemilik": "John Doe",
    "alamat": "Jakarta",
    "merkKendaraan": "Honda",
    "tahunPembuatan": 2020,
    "kapSilinder": 150,
    "warnaKendaraan": "Hitam",
    "bahanBakar": "Bensin"
  }
  ```
- ### Update kendaraan
  - PUT /api/v1/vehicles/{nrk}
- ### Delete kendaraan
  - DELETE /api/v1/vehicles/{nrk}

Respons sukses mengikuti format:

```json
{
  "status": 200,
  "success": true,
  "message": "Success",
  "timestamp": "2025-06-18 15:30:22",
  "resource": [ … ]   // atau objek tunggal
}
```

## Kontribusi
- Fork repository ini
- Buat branch fitur Anda (git checkout -b feature/<nama>)
- Commit perubahan (git commit -m 'Add feature...')
- Push ke branch Anda (git push origin feature/<nama>)
- Buat Pull Request

## Lisensi
This project is licensed under the GNU General Public License version 3 (GPLv3). See the LICENSE file for full details. [GPL3](https://www.gnu.org/licenses/gpl-3.0.txt)

