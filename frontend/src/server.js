import ky from "ky";

/**
 *  Fetching all vehicles data.
 *
 * @param {string} query - Querying data like nrk, nama pemilik, and merk kendaraan.
 *
 * @returns {Promise<any>} return JSON response.
 */
export async function fetchVehiclesData(query = "") {
  try {
    const response = await ky
      .get(
        `${import.meta.env.VITE_API_URL}/api/v1/vehicles?q=${encodeURIComponent(query)}`
      )
      .json();
    return response;
  } catch (error) {
    let errorMessage =
      error instanceof Error
        ? error.message
        : `Error: An unknown error occurred ⛔️`;

    console.error(errorMessage);
    throw error;
  }
}

/**
 * Fetching one vehicle data.
 *
 * @param {string} nrk - Nomor Registrasi Kendaraan
 *
 * @returns {Promise<any>} return JSON response.
 */
export async function fetchVehicleData(nrk) {
  try {
    const response = await ky
      .get(`${import.meta.env.VITE_API_URL}/api/v1/vehicles/${nrk}`)
      .json();
    return response;
  } catch (error) {
    let errorMessage =
      error instanceof Error
        ? error.message
        : `Error: An unknown error occurred ⛔️`;

    console.error(errorMessage);
    throw error;
  }
}

/**
 * Create new vehicle data.
 *
 * @param {object} request - Request body for create new vehicle.
 */
export async function createVehicleData(request) {
  try {
    const response = await ky
      .post(`${import.meta.env.VITE_API_URL}/api/v1/vehicles`, {
        json: {
          nrk: request.nrk,
          namaPemilik: request.namaPemilik,
          alamat: request.alamat,
          merkKendaraan: request.merkKendaraan,
          tahunPembuatan: request.tahunPembuatan,
          kapSilinder: request.kapSilinder,
          warnaKendaraan: request.warnaKendaraan,
          bahanBakar: request.bahanBakar,
        },
        retry: 3,
      })
      .json();
    return response;
  } catch (error) {
    let errorMessage =
      error instanceof Error
        ? error.message
        : `Error: An unknown error occurred ⛔️`;

    console.error(errorMessage);
    throw error;
  }
}

/**
 * Update vehicle data.
 *
 * @param {string} nrk - Nomor Registrasi Kendaraan
 * @param {object} request - Request body for update vehicle.
 */
export async function updateVehicleData(nrk, request) {
  try {
    const response = await ky
      .put(`${import.meta.env.VITE_API_URL}/api/v1/vehicles/${nrk}`, {
        json: {
          nrk: request.nrk,
          namaPemilik: request.namaPemilik,
          alamat: request.alamat,
          merkKendaraan: request.merkKendaraan,
          tahunPembuatan: request.tahunPembuatan,
          kapSilinder: request.kapSilinder,
          warnaKendaraan: request.warnaKendaraan,
          bahanBakar: request.bahanBakar,
        },
        retry: 3,
      })
      .json();
    return response;
  } catch (error) {
    let errorMessage =
      error instanceof Error
        ? error.message
        : `Error: An unknown error occurred ⛔️`;

    console.error(errorMessage);
    throw error;
  }
}

/**
 * Hard delete vehicle data.
 *
 * @param {string} nrk - Nomor Registrasi Kendaraan
 */
export async function deleteVehicleData(nrk) {
  try {
    const response = await ky
      .delete(`${import.meta.env.VITE_API_URL}/api/v1/vehicles/${nrk}`)
      .json();
    return response;
  } catch (error) {
    let errorMessage =
      error instanceof Error
        ? error.message
        : `Error: An unknown error occurred ⛔️`;

    console.error(errorMessage);
    throw error;
  }
}
