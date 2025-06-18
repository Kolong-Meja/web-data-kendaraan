import "./style.css";

import {
  fetchVehiclesData,
  createVehicleData,
  deleteVehicleData,
  updateVehicleData,
  fetchVehicleData,
} from "./server";

import MicroModal from "micromodal";

import homeTemplate from "./views/home.html?raw";
import { _defaultTableHeaders } from "./constants";

const app = document.querySelector("#app");
app.innerHTML = homeTemplate;

MicroModal.init({
  disableScroll: false,
  disableFocus: false,
  awaitCloseAnimation: true,
});

let _targetDeleteNrk = null;
let _targetUpdateNrk = null;
let _targetViewNrk = null;

/**
 * Render vehicle table interface.
 *
 * @returns {Promise<void>}
 */
async function displayVehiclesData() {
  const response = await fetchVehiclesData();
  const data = response.resource || [];

  const containerElement = document.querySelector("#home");
  if (!containerElement) {
    console.error("#home element not found.");
    return;
  }

  const tableHeaderElement = document.querySelector("#table-header");
  if (!tableHeaderElement) {
    console.error("#table-header element not found.");
    return;
  }

  const tableBodyElement = document.querySelector("#table-body");
  if (!tableBodyElement) {
    console.error("#table-body element not found.");
    return;
  }

  tableHeaderElement.innerHTML = `
     ${_defaultTableHeaders
       .map(
         (h) =>
           `<th class="px-2 sm:px-4 py-4 text-center text-xs sm:text-sm font-medium text-gray-600 uppercase whitespace-nowrap tracking-wider">
                ${h === "actions" ? "Actions" : h}
              </th>`
       )
       .join("")}
  `;

  data.forEach((item, index) => {
    const tableRowElement = document.createElement("tr");
    tableRowElement.classList.add(
      "hover:bg-gray-100",
      "odd:bg-white",
      "even:bg-gray-50",
      "transition-colors",
      "duration-300",
      "ease-in-out"
    );

    tableRowElement.innerHTML = `
      <td class="px-6 py-4 whitespace-nowrap text-sm text-center text-gray-900">${index + 1}</td>
      <td class="px-6 py-4 whitespace-nowrap text-sm text-center text-gray-900">${item.nrk}</td>
      <td class="px-6 py-4 whitespace-nowrap text-sm text-center text-gray-900">${item.namaPemilik}</td>
      <td class="px-6 py-4 whitespace-nowrap text-sm text-center text-gray-900">${item.merkKendaraan}</td>
      <td class="px-6 py-4 whitespace-nowrap text-sm text-center text-gray-900">${item.tahunPembuatan}</td>
      <td class="px-6 py-4 whitespace-nowrap text-sm text-center text-gray-900">${item.kapSilinder} cc</td>
      <td class="px-6 py-4 whitespace-nowrap text-sm text-center text-gray-900">${item.warnaKendaraan}</td>
      <td class="px-6 py-4 whitespace-nowrap text-sm text-center text-gray-900">${item.bahanBakar}</td>
      <td class="px-6 py-4 whitespace-nowrap text-sm text-center font-medium space-x-2">
        <button data-nrk="${item.nrk}" class="viewButton border-blue-400 border-2 hover:border-blue-500 hover:bg-blue-500 text-blue-400 hover:text-gray-50 px-2 py-1 rounded transition-colors duration-300 ease-in-out">View</button>
        <button data-nrk="${item.nrk}" class="editButton border-yellow-400 border-2 hover:border-yellow-500 hover:bg-yellow-500 text-yellow-400 hover:text-gray-50 px-2 py-1 rounded transition-colors duration-300 ease-in-out">Edit</button>
        <button data-nrk="${item.nrk}" class="deleteButton border-red-400 border-2 hover:bg-red-500 hover:border-red-500 text-red-400 hover:text-gray-50 px-2 py-1 rounded transition-colors duration-300 ease-in-out">Delete</button>
      </td>
    `;

    tableRowElement
      .querySelector(".viewButton")
      ?.addEventListener("click", () => {
        console.log("View button called: ", item.nrk);

        _targetViewNrk = item.nrk;

        fetchVehicleData(_targetViewNrk)
          .then((res) => {
            const item = res.resource;

            document.getElementById("view-nrk").innerText = item.nrk;
            document.getElementById("view-namaPemilik").innerText =
              item.namaPemilik;
            document.getElementById("view-alamat").innerText = item.alamat;
            document.getElementById("view-merkKendaraan").innerText =
              item.merkKendaraan;
            document.getElementById("view-tahunPembuatan").innerText =
              item.tahunPembuatan;
            document.getElementById("view-kapSilinder").innerText =
              item.kapSilinder + " cc";
            document.getElementById("view-warnaKendaraan").innerText =
              item.warnaKendaraan;
            document.getElementById("view-bahanBakar").innerText =
              item.bahanBakar;

            MicroModal.show("modal-view");
          })
          .catch(console.error);
      });

    tableRowElement
      .querySelector(".editButton")
      ?.addEventListener("click", () => {
        console.log("Edit button called: ", item.nrk);

        _targetUpdateNrk = item.nrk;

        fetchVehiclesData()
          .then((res) => {
            const item = res.resource.find((v) => v.nrk === _targetUpdateNrk);
            if (!item) return;

            const form = document.getElementById("edit-form");
            form.nrk.value = item.nrk;
            form.namaPemilik.value = item.namaPemilik;
            form.alamat.value = item.alamat;
            form.merkKendaraan.value = item.merkKendaraan;
            form.tahunPembuatan.value = item.tahunPembuatan;
            form.kapSilinder.value = item.kapSilinder;
            form.warnaKendaraan.value = item.warnaKendaraan;
            form.bahanBakar.value = item.bahanBakar;

            MicroModal.show("modal-edit");
          })
          .catch(console.error);
      });

    tableRowElement
      .querySelector(".deleteButton")
      ?.addEventListener("click", () => {
        console.log("Delete button called: ", item.nrk);

        _targetDeleteNrk = item.nrk;

        document.getElementById("delete-item-nrk").innerHTML = _targetDeleteNrk;
        MicroModal.show("modal-delete");
      });

    tableBodyElement.appendChild(tableRowElement);
  });
}

document.addEventListener("DOMContentLoaded", async () => {
  await displayVehiclesData();

  // create data.
  const createButton = document.querySelector(".createButton");
  createButton?.addEventListener("click", () => {
    MicroModal.show("modal-create");
  });

  const form = document.querySelector("#create-form");
  form?.addEventListener("submit", async (event) => {
    event.preventDefault();

    const formData = new FormData(form);
    const payload = {
      nrk: formData.get("nrk"),
      namaPemilik: formData.get("namaPemilik"),
      alamat: formData.get("alamat"),
      merkKendaraan: formData.get("merkKendaraan"),
      tahunPembuatan: Number(formData.get("tahunPembuatan")),
      kapSilinder: Number(formData.get("kapSilinder")),
      warnaKendaraan: formData.get("warnaKendaraan"),
      bahanBakar: formData.get("bahanBakar"),
    };

    try {
      await createVehicleData(payload);

      MicroModal.close("modal-create");

      form.reset();

      await fetchVehiclesData();
    } catch (error) {
      console.error("Create vehicle failed ❌: ", error);
    }
  });

  // update data.
  const editForm = document.querySelector("#edit-form");
  editForm?.addEventListener("submit", async (event) => {
    event.preventDefault();
    if (_targetUpdateNrk) return;

    const formData = new FormData(editForm);
    const payload = {
      nrk: formData.get("nrk"),
      namaPemilik: formData.get("namaPemilik"),
      alamat: formData.get("alamat"),
      merkKendaraan: formData.get("merkKendaraan"),
      tahunPembuatan: Number(formData.get("tahunPembuatan")),
      kapSilinder: Number(formData.get("kapSilinder")),
      warnaKendaraan: formData.get("warnaKendaraan"),
      bahanBakar: formData.get("bahanBakar"),
    };

    try {
      await updateVehicleData(_targetUpdateNrk, payload);

      MicroModal.close("modal-edit");

      form.reset();

      await fetchVehiclesData();
    } catch (err) {
      console.error("Update vehicle failed ❌:", err);
    }
  });
});

document
  .getElementById("confirm-delete-button")
  ?.addEventListener("click", async () => {
    if (!_targetDeleteNrk) return;
    try {
      await deleteVehicleData(_targetDeleteNrk);
      MicroModal.close("modal-delete");

      await fetchVehiclesData();
    } catch (err) {
      console.error("Delete vehicle failed ❌: ", err);
    }
  });
