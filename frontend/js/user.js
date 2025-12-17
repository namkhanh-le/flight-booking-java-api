const userTable = document.getElementById("user-table");
const userForm = document.getElementById("user-form");

async function loadUsers() {
    const response = await fetch(`${BASE_URL}/user`);
    if (!response.ok) return;

    const users = await response.json();
    userTable.innerHTML = "";

    users.forEach(u => {
        const row = document.createElement("tr");
        row.innerHTML = `
            <td>${u.id}</td>
            <td>${u.firstName}</td>
            <td>${u.lastName}</td>
            <td>${u.email}</td>
            <td>${u.address}</td>
            <td>${u.phoneNumber}</td>
            <td>${u.birthDate}</td>
            <td><button onclick="deleteUser(${u.id})">Delete</button></td>
        `;
        userTable.appendChild(row);
    });
}

userForm.addEventListener("submit", async (e) => {
    e.preventDefault();

    const user = {
        firstName: document.getElementById("user-firstname").value,
        lastName: document.getElementById("user-lastname").value,
        email: document.getElementById("user-email").value,
        address: document.getElementById("user-address").value,
        phoneNumber: document.getElementById("user-phone").value,
        birthDate: document.getElementById("user-birthdate").value
    };

    await fetch(`${BASE_URL}/user`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(user)
    });

    userForm.reset();
    loadUsers();
});

async function deleteUser(userId) {
    const response = await fetch(`${BASE_URL}/user/${userId}`, {
        method: "DELETE"
    });

    if (response.ok || response.status === 204) {
        loadUsers();
    }   
}
