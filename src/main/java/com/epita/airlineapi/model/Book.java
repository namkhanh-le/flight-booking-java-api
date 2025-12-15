package com.epita.airlineapi.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "bookings")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReservation;

    @ManyToOne
    @JoinColumn(name = "flight_id", nullable = false)
    private Flight flight;

    @ManyToOne
    @JoinColumn(name = "client_passport", nullable = false)
    private Client client;

    @Column(nullable = false)
    private String typeOfSeat;  // "FIRST_CLASS", "PREMIUM", "BUSINESS", "ECONOMY"

    @Column(nullable = false)
    private LocalDateTime bookingDate;

    @Column(nullable = false)
    private String bookingStatus;  // "CONFIRMED", "CANCELLED", "PENDING"

    // Default constructor
    public Book() {
        this.bookingDate = LocalDateTime.now();
        this.bookingStatus = "CONFIRMED";
    }

    // Constructor with fields
    public Book(Long idReservation, Flight flight, Client client, String typeOfSeat, LocalDateTime bookingDate, String bookingStatus) {
        this.idReservation = idReservation;
        this.flight = flight;
        this.client = client;
        this.typeOfSeat = typeOfSeat;
        this.bookingDate = bookingDate;
        this.bookingStatus = bookingStatus;
    }

    // Getters and Setters
    public Long getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(Long idReservation) {
        this.idReservation = idReservation;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getTypeOfSeat() {
        return typeOfSeat;
    }

    public void setTypeOfSeat(String typeOfSeat) {
        this.typeOfSeat = typeOfSeat;
    }

    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDateTime bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(idReservation, book.idReservation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idReservation);
    }
}