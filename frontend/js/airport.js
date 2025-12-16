const airportTable = document.getElementById("airport-table");
const airportForm = document.getElementById("airport-form");

async function loadAirports() {
    const response = await fetch(`${BASE_URL}/airport`);
    if (response.status === 204) {
        airportTable.innerHTML = "";
        return;
    }
    const airports = await response.json();
    airportTable.innerHTML = "";

    airports.forEach(a => {
        const row = document.createElement("tr");
        row.innerHTML = `
            <td>${a.airportId}</td>
            <td>${a.airportName}</td>
            <td>${a.airportCity}</td>
            <td>${a.airportCountry}</td>
            <td><button onclick="deleteAirport(${a.airportId})">Delete</button></td>
        `;
        airportTable.appendChild(row);
    });
}

airportForm.addEventListener("submit", async (e) => {
    e.preventDefault();

    const airport = {
        airportName: document.getElementById("airportName").value,
        airportCity: document.getElementById("airportCity").value,
        airportCountry: document.getElementById("airportCountry").value
    };

    await fetch(`${BASE_URL}/airport`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(airport)
    });

    airportForm.reset();
    loadAirports();
});

async function deleteAirport(id) {
    await fetch(`${BASE_URL}/airport/${id}`, {
        method: "DELETE"
    });
    loadAirports();
}

loadAirports();
