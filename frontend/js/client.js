const clientTable = document.getElementById("client-table");
const clientForm = document.getElementById("client-form");
const clientUserSelect = document.getElementById("client-user");

async function loadClients() {
    const response = await fetch(`${BASE_URL}/client`);
    if (!response.ok) return;

    const clients = await response.json();
    clientTable.innerHTML = "";

    clients.forEach(c => {
        const row = document.createElement("tr");
        row.innerHTML = `
            <td>${c.passportNumber}</td>
            <td>${c.user.id}</td>
            <td>${c.user.firstName} ${c.user.lastName}</td>
            <td>${c.user.email}</td>
            <td>${c.user.address}</td>
            <td>${c.user.phoneNumber}</td>
            <td>${c.user.birthDate}</td>
            <td><button onclick="deleteClient('${c.passportNumber}')">Delete</button></td>
        `;
        clientTable.appendChild(row);
    });
}

async function loadUsersForClients() {
    const response = await fetch(`${BASE_URL}/user`);
    if (!response.ok) return;

    const users = await response.json();
    clientUserSelect.innerHTML = "";

    // add a placeholder option so user must choose one
    const placeholder = document.createElement("option");
    placeholder.value = "";
    placeholder.textContent = "Select a user...";
    placeholder.disabled = true;
    placeholder.selected = true;
    clientUserSelect.appendChild(placeholder);

    users.forEach(u => {
        const option = document.createElement("option");
        option.value = u.id;
        option.textContent = `${u.firstName} ${u.lastName} (ID ${u.id})`;
        clientUserSelect.appendChild(option);
    });
}

clientForm.addEventListener("submit", async (e) => {
    e.preventDefault();

    const selectedUserId = Number(clientUserSelect.value);
    if (!selectedUserId) {
        alert("Please select a user for the client.");
        return;
    }

    const client = {
        passportNumber: document.getElementById("client-passport").value,
        user: { id: selectedUserId }
    };

    await fetch(`${BASE_URL}/client`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(client)
    });

    clientForm.reset();
    loadClients();
});

async function deleteClient(passportNumber) {
    const response = await fetch(`${BASE_URL}/client/${passportNumber}`, {
        method: "DELETE"
    });

    if (response.ok || response.status === 204) {
        loadClients();
    } else {
        const errorText = await response.text();
        alert("Delete client failed: " + errorText);
    }
}
