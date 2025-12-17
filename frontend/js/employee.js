const employeeForm = document.getElementById('employee-form');
const employeeTable = document.getElementById('employee-table-body');
const employeeUserSelect = document.getElementById('employee-user');

async function loadEmployees() {
    const response = await fetch(`${BASE_URL}/employee`);
    if (!response.ok) return;

    const employees = await response.json();
    employeeTable.innerHTML = "";

    employees.forEach(e => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${e.employeeNumber}</td>
            <td>${e.profession}</td>
            <td>${e.title}</td>
            <td>${e.user.id}</td>
            <td>${e.user.firstName} ${e.user.lastName}</td>
            <td>
                <button onclick="deleteEmployee(${e.employeeNumber})">Delete</button>
            </td>
        `;
        employeeTable.appendChild(row);
    });
}

async function loadUsersForEmployees() {
    const response = await fetch(`${BASE_URL}/user`);
    if (!response.ok) return;

    const users = await response.json();
    employeeUserSelect.innerHTML = "";

    const placeholder = document.createElement("option");
    placeholder.value = "";
    placeholder.textContent = "Select a user...";
    placeholder.disabled = true;
    placeholder.selected = true;
    employeeUserSelect.appendChild(placeholder);

    users.forEach(u => {
        const option = document.createElement("option");
        option.value = u.id;
        option.textContent = `${u.firstName} ${u.lastName} (ID ${u.id})`;
        employeeUserSelect.appendChild(option);
    });
}

employeeForm.addEventListener('submit', async (e) => {
    e.preventDefault();

    const employee = {
        profession: document.getElementById('employee-profession').value,
        title: document.getElementById('employee-title').value,
        user: {
            id: parseInt(employeeUserSelect.value)
        }
    };

    console.log("Employee payload:", employee);

    await fetch(`${BASE_URL}/employee`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(employee)
    });

    employeeForm.reset();
    loadEmployees();
});

async function deleteEmployee(employeeNumber) {
    const response = await fetch(`${BASE_URL}/employee/${employeeNumber}`, {
        method: 'DELETE'
    });

    if (response.ok || response.status === 204) {
        loadEmployees();
    } else {
        const errorText = await response.text();
        alert('Delete employee failed: ' + errorText);
    }
}

document.addEventListener("DOMContentLoaded", () => {
    loadEmployees();
    loadUsersForEmployees();
});
