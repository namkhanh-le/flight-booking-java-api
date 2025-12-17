const milesTable = document.getElementById("miles-table");
const milesInfo = document.getElementById("miles-info");
const milesPassportInput = document.getElementById("miles-passport");

async function loadAllMiles() {
    const response = await fetch(`${BASE_URL}/miles-reward`);

    if (response.status === 204) {
        milesTable.innerHTML = "";
        return;
    }

    if (!response.ok) {
        milesInfo.textContent = "Failed to load rewards";
        return;
    }

    const rewards = await response.json();
    renderMiles(rewards);
}

async function loadMilesByClient() {
    const passport = milesPassportInput.value;
    if (!passport) return;

    const response = await fetch(`${BASE_URL}/miles-reward/client/${passport}`);

    if (response.status === 204) {
        milesTable.innerHTML = "";
        milesInfo.textContent = "No rewards for this client";
        return;
    }

    if (!response.ok) {
        milesInfo.textContent = "Client not found";
        return;
    }

    const rewards = await response.json();
    renderMiles(rewards);
}

async function loadFlightCount() {
    const passport = milesPassportInput.value;
    if (!passport) return;

    const response = await fetch(`${BASE_URL}/miles-reward/client/${passport}/flight-count`);
    if (!response.ok) {
        milesInfo.textContent = "Client not found";
        return;
    }

    const data = await response.json();
    milesInfo.innerHTML = `
        Flights this year: <b>${data.flightsThisYear}</b><br>
        Flights needed for discount: <b>${data.needsMoreForDiscount}</b>
    `;
}

async function loadDiscount() {
    const passport = milesPassportInput.value;
    if (!passport) return;

    const response = await fetch(`${BASE_URL}/miles-reward/client/${passport}/discount`);
    if (!response.ok) {
        milesInfo.textContent = "Client not found";
        return;
    }

    const data = await response.json();

    if (data.hasDiscount) {
        milesInfo.innerHTML = `
            Discount available<br>
            Code: <b>${data.discountCode}</b>
        `;
    } else {
        milesInfo.innerHTML = "No discount available yet";
    }
}

function renderMiles(rewards) {
    milesTable.innerHTML = "";
    milesInfo.textContent = "";

    rewards.forEach(r => {
        const row = document.createElement("tr");
        row.innerHTML = `
            <td>${r.id}</td>
            <td>${r.client.passportNumber}</td>
            <td>${r.flight.flightId}</td>
            <td>${r.date}</td>
            <td>${r.discountCode ?? "-"}</td>
        `;
        milesTable.appendChild(row);
    });
}
