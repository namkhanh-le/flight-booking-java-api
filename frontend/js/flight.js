const flightTable = document.getElementById("flight-table");

async function loadFlights() {
    const response = await fetch(`${BASE_URL}/flight`);
    if (response.status === 204) {
        flightTable.innerHTML = "";
        return;
    }

    const flights = await response.json();
    flightTable.innerHTML = "";

    flights.forEach(f => {
        const row = document.createElement("tr");
        row.innerHTML = `
            <td>${f.flightId}</td>
            <td>${f.flightNumber}</td>
            <td>${f.departureCity}</td>
            <td>${f.arrivalCity}</td>
            <td>${f.departureDate}</td>
            <td>${f.arrivalDate}</td>
            <td>${f.departureAirport}</td>
            <td>${f.arrivalAirport}</td>
            <td>${f.numberOfSeats}</td>
            <td>$${f.businessPrice}</td>
            <td>$${f.economyPrice}</td>
            <td><button onclick="deleteFlight(${f.flightId})">Delete</button></td>
        `;
        flightTable.appendChild(row);
    });
}

async function deleteFlight(id) {
    await fetch(`${BASE_URL}/flight/${id}`, {
        method: "DELETE"
    });
    loadFlights();
}
