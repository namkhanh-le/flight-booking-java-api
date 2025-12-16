const planeTable = document.getElementById("plane-table");

async function loadPlanes() {
    const response = await fetch(`${BASE_URL}/plane`);
    if (response.status === 204) {
        flightTable.innerHTML = "";
        return;
    }

    const planes = await response.json();
    planeTable.innerHTML = "";

    planes.forEach(p => {
        const row = document.createElement("tr");
        row.innerHTML = `
            <td>${p.planeId}</td>
            <td>${p.planeBrand}</td>
            <td>${p.planeModel}</td>
            <td>${p.manufacturingYear}</td>
            <td>
                <button onclick="deletePlane(${p.planeId})">Delete</button>
            </td>
        `;
        planeTable.appendChild(row);
    });
}

async function deletePlane(id) {
    await fetch(`${BASE_URL}/plane/${id}`, {
        method: "DELETE"
    });
    loadPlanes();
}
