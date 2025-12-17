package com.epita.airlineapi.service;

import com.epita.airlineapi.model.Book;
import com.epita.airlineapi.model.Client;
import com.epita.airlineapi.model.Flight;
import com.epita.airlineapi.repository.BookRepository;
import com.epita.airlineapi.repository.ClientRepository;
import com.epita.airlineapi.repository.FlightRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final FlightRepository flightRepository;
    private final ClientRepository clientRepository;
    private final MilesRewardService milesRewardService;

    public BookService(BookRepository bookRepository, FlightRepository flightRepository, ClientRepository clientRepository, MilesRewardService milesRewardService) {
        this.bookRepository = bookRepository;
        this.flightRepository = flightRepository;
        this.clientRepository = clientRepository;
        this.milesRewardService = milesRewardService;
    }

    // Get all bookings
    public List<Book> getAllBookings() {
        return bookRepository.findAll();
    }

    // Get one booking by ID
    public Optional<Book> getBookingById(Long idReservation) {
        return bookRepository.findById(idReservation);
    }

    // Get all bookings for a specific client
    public List<Book> getBookingsByClientPassport(String passportNumber) {
        return bookRepository.findByClientPassportNumber(passportNumber);
    }

    // Get all bookings for a specific flight
    public List<Book> getBookingsByFlight(Flight flight) {
        return bookRepository.findByFlight(flight);
    }

    // Create a new booking with validation
    @Transactional
    public Book createBooking(Book book) {
        // Validate flight exists
        if (book.getFlight() == null || book.getFlight().getFlightId() == null) {
            throw new IllegalArgumentException("Flight is required");
        }

        Long flightId = book.getFlight().getFlightId();
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new EntityNotFoundException("Flight not found with ID: " + flightId));

        // Validate client exists
        if (book.getClient() == null || book.getClient().getPassportNumber() == null) {
            throw new IllegalArgumentException("Client is required");
        }

        String passportNumber = book.getClient().getPassportNumber();
        Client client = clientRepository.findById(passportNumber)
                .orElseThrow(() -> new EntityNotFoundException("Client not found with passport: " + passportNumber));

        // Check seat availability
        long currentBookings = bookRepository.countByFlight(flight);
        if (currentBookings >= flight.getNumberOfSeats()) {
            throw new IllegalStateException("No seats available on this flight. Flight is fully booked.");
        }

        // Validate seat type
        String seatType = book.getTypeOfSeat();
        if (seatType == null || seatType.isEmpty()) {
            throw new IllegalArgumentException("Seat type is required");
        }

        if (!seatType.equals("FIRST_CLASS") && !seatType.equals("PREMIUM") &&
                !seatType.equals("BUSINESS") && !seatType.equals("ECONOMY")) {
            throw new IllegalArgumentException("Invalid seat type. Must be: FIRST_CLASS, PREMIUM, BUSINESS, or ECONOMY");
        }

        // Set the validated entities
        book.setFlight(flight);
        book.setClient(client);

        // Set booking date and status if not set
        if (book.getBookingDate() == null) {
            book.setBookingDate(LocalDateTime.now());
        }

        if (book.getBookingStatus() == null || book.getBookingStatus().isEmpty()) {
            book.setBookingStatus("CONFIRMED");
        }

        Book savedBooking = bookRepository.save(book);

        milesRewardService.recordFlight(
                book.getClient(),
                book.getFlight()
        );
        // Save the booking
        return bookRepository.save(book);
    }

    // Delete a booking
    public void deleteBooking(Long idReservation) {
        boolean exists = bookRepository.existsById(idReservation);

        if (!exists) {
            throw new EntityNotFoundException("Booking with ID " + idReservation + " does not exist");
        }

        bookRepository.deleteById(idReservation);
    }

    // Update a booking
    @Transactional
    public void updateBooking(Long idReservation, Book updateRequest) {
        // Retrieve existing booking
        Book book = bookRepository.findById(idReservation)
                .orElseThrow(() -> new IllegalStateException("Booking with ID " + idReservation + " does not exist"));

        // Update seat type
        if (updateRequest.getTypeOfSeat() != null &&
                !updateRequest.getTypeOfSeat().isEmpty() &&
                !Objects.equals(book.getTypeOfSeat(), updateRequest.getTypeOfSeat())) {

            // Validate seat type
            String seatType = updateRequest.getTypeOfSeat();
            if (!seatType.equals("FIRST_CLASS") && !seatType.equals("PREMIUM") &&
                    !seatType.equals("BUSINESS") && !seatType.equals("ECONOMY")) {
                throw new IllegalArgumentException("Invalid seat type");
            }

            book.setTypeOfSeat(updateRequest.getTypeOfSeat());
        }

        // Update booking status
        if (updateRequest.getBookingStatus() != null &&
                !updateRequest.getBookingStatus().isEmpty() &&
                !Objects.equals(book.getBookingStatus(), updateRequest.getBookingStatus())) {
            book.setBookingStatus(updateRequest.getBookingStatus());
        }

        // Note: We don't allow changing flight or client after booking is created
    }

    // Cancel a booking (change status to CANCELLED)
    @Transactional
    public void cancelBooking(Long idReservation) {
        Book book = bookRepository.findById(idReservation)
                .orElseThrow(() -> new EntityNotFoundException("Booking with ID " + idReservation + " does not exist"));

        book.setBookingStatus("CANCELLED");
    }

    // Check if seats are available for a flight
    public boolean areSeatsAvailable(Long flightId) {
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new EntityNotFoundException("Flight not found"));

        long currentBookings = bookRepository.countByFlight(flight);
        return currentBookings < flight.getNumberOfSeats();
    }

    // Get available seats count for a flight
    public int getAvailableSeats(Long flightId) {
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new EntityNotFoundException("Flight not found"));

        long currentBookings = bookRepository.countByFlight(flight);
        return (int) (flight.getNumberOfSeats() - currentBookings);
    }
}