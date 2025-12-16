const bookingForm = document.getElementById("booking-form");
const bookingResult = document.getElementById("booking-result");

bookingForm.addEventListener("submit", async (e) => {
    e.preventDefault();

    const flightId = parseInt(document.getElementById("booking-flight").value);
    const passportNumber = document.getElementById("booking-passport").value;
    const seatType = document.getElementById("booking-seat").value;

    const booking = {
        flight: {
            flightId: flightId
        },
        client: {
            passportNumber: passportNumber
        },
        typeOfSeat: seatType
    };

    console.log("Booking payload sent to backend:", booking);

    try {
        const response = await fetch(`${BASE_URL}/book`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(booking)
        });

        if (response.ok) {
            const data = await response.json();
            bookingResult.textContent = "Booking successful (ID " + data.idReservation + ")";
        } else {
            const errorText = await response.text();
            bookingResult.textContent = "Booking failed: " + errorText;
        }

    } catch (error) {
        bookingResult.textContent = "Booking failed: network error";
        console.error(error);
    }
});

const bookingTable = document.getElementById("booking-table");

async function loadBookings() {
    const response = await fetch(`${BASE_URL}/book`);

    if (response.status === 204) {
        bookingTable.innerHTML = "";
        return;
    }

    if (!response.ok) {
        bookingTable.innerHTML = "<tr><td colspan='4'>Failed to load bookings</td></tr>";
        return;
    }

    const bookings = await response.json();
    bookingTable.innerHTML = "";

    bookings.forEach(b => {
        const row = document.createElement("tr");
        row.innerHTML = `
            <td>${b.idReservation}</td>
            <td>${b.flight.flightNumber}</td>
            <td>${b.client.passportNumber}</td>
            <td>${b.client.user.firstName} ${b.client.user.lastName}</td>
            <td>${b.typeOfSeat}</td>
            <td><button onclick="deleteBooking(${b.idReservation})">Delete</button></td>
        `;
        bookingTable.appendChild(row);
    });
}

async function deleteBooking(idReservation) {
    const response = await fetch(`${BASE_URL}/book/${idReservation}`, {
        method: "DELETE"
    });

    if (response.ok || response.status === 204) {
        loadBookings();
    } else {
        const errorText = await response.text();
        alert("Delete failed: " + errorText);
    }
}
