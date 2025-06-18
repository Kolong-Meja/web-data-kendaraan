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
let currentQuery = "";
let searchTimeout = null;

const tableBodyElement = document.querySelector("#table-body");
const tableHeaderElement = document.querySelector("#table-header");
const searchInput = document.getElementById("search-input");
const createButton = document.querySelector(".createButton");
const createForm = document.querySelector("#create-form");
const editForm = document.querySelector("#edit-form");
const confirmDeleteButton = document.querySelector("#confirm-delete-button");
// const clearSearchButton = document.getElementById("clear-search-button");

function renderTableHeader() {
  if (!tableHeaderElement) return;
  tableHeaderElement.innerHTML = _defaultTableHeaders
    .map(
      (h) =>
        `<th class="px-2 sm:px-4 py-4 text-center text-xs sm:text-sm font-medium text-gray-600 uppercase whitespace-nowrap tracking-wider">
                ${h === "actions" ? "Actions" : h}
              </th>`
    )
    .join("");
}

async function generateSearch(query) {
  if (!tableBodyElement) {
    console.error("#table-body element not found.");
    return;
  }

  tableBodyElement.innerHTML = `
        <tr>
          <td colspan="9" class="text-center py-4">Loading...</td>
        </tr>`;

  try {
    const response = await fetchVehiclesData(query);
    const data = response.resource || [];

    tableBodyElement.innerHTML = "";

    if (data.length === 0) {
      tableBodyElement.innerHTML = `
          <tr>
            <td colspan="9" class="text-center py-4">No vehicles found</td>
          </tr>`;
      return;
    }

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
      addRowListeners(tableRowElement, item);

      tableBodyElement.appendChild(tableRowElement);
    });
  } catch (error) {
    console.error("Error searching vehicles:", error);

    tableBodyElement.innerHTML = `
      <tr><td colspan="9" class="text-center py-4 text-red-500">
        Error loading data: ${error.message}
      </td></tr>`;
  }
}

function debounceSearch(query) {
  clearTimeout(searchTimeout);
  searchTimeout = setTimeout(() => {
    generateSearch(query);
  }, 800);
}

// function toggleClearButton(query) {
//   clearSearchButton.style.display = query.trim() ? "block" : "none";
// }

function addRowListeners(tableRow, item) {
  tableRow.querySelector(".viewButton").addEventListener("click", () => {
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
        document.getElementById("view-bahanBakar").innerText = item.bahanBakar;

        MicroModal.show("modal-view");
      })
      .catch(console.error);
  });

  tableRow.querySelector(".editButton").addEventListener("click", () => {
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

  tableRow.querySelector(".deleteButton").addEventListener("click", () => {
    console.log("Delete button called: ", item.nrk);

    _targetDeleteNrk = item.nrk;

    document.getElementById("delete-item-nrk").innerHTML = _targetDeleteNrk;
    MicroModal.show("modal-delete");
  });
}

function setupAllEventListeners() {
  searchInput?.addEventListener("input", (event) => {
    // toggleClearButton(query);
    debounceSearch(event.target.value);
  });

  // clearSearchButton.addEventListener("click", () => {
  //   searchInput.value = "";
  //   toggleClearButton("");
  //   generateSearch("");
  // });

  createButton?.addEventListener("click", () =>
    MicroModal.show("modal-create")
  );
  createForm?.addEventListener("submit", async (event) => {
    event.preventDefault();

    const formData = new FormData(createForm);
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

      createForm.reset();

      await generateSearch(currentQuery);
    } catch (error) {
      console.error("Create vehicle failed ❌: ", error);
    }
  });

  editForm?.addEventListener("submit", async (event) => {
    event.preventDefault();
    if (!_targetUpdateNrk) return;

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

      _targetUpdateNrk = null;

      await generateSearch(currentQuery);
    } catch (err) {
      console.error("Update vehicle failed ❌:", err);
    }
  });

  confirmDeleteButton?.addEventListener("click", async () => {
    if (!_targetDeleteNrk) return;
    try {
      await deleteVehicleData(_targetDeleteNrk);

      MicroModal.close("modal-delete");

      _targetDeleteNrk = null;

      await generateSearch(currentQuery);
    } catch (err) {
      console.error("Delete vehicle failed ❌: ", err);
    }
  });
}

renderTableHeader();
setupAllEventListeners();
generateSearch("");
