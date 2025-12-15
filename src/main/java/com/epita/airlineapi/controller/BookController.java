package com.epita.airlineapi.controller;

import com.epita.airlineapi.model.Book;
import com.epita.airlineapi.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/v1/book")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    // GET all bookings
    @GetMapping
    public ResponseEntity<?> getAllBookings() {
        List<Book> bookings = bookService.getAllBookings();

        if (bookings.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(bookings, HttpStatus.OK);
    }

    // GET one booking by ID
    @GetMapping(path = "{idReservation}")
    public ResponseEntity<?> getBookingById(@PathVariable Long idReservation) {
        Optional<Book> booking = bookService.getBookingById(idReservation);

        return booking.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // GET bookings by client passport
    @GetMapping(path = "/client/{passportNumber}")
    public ResponseEntity<?> getBookingsByClient(@PathVariable String passportNumber) {
        List<Book> bookings = bookService.getBookingsByClientPassport(passportNumber);

        if (bookings.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(bookings, HttpStatus.OK);
    }

    // GET available seats for a flight
    @GetMapping(path = "/flight/{flightId}/available-seats")
    public ResponseEntity<?> getAvailableSeats(@PathVariable Long flightId) {
        try {
            int availableSeats = bookService.getAvailableSeats(flightId);
            boolean hasSeats = bookService.areSeatsAvailable(flightId);

            Map<String, Object> response = new HashMap<>();
            response.put("flightId", flightId);
            response.put("availableSeats", availableSeats);
            response.put("seatsAvailable", hasSeats);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // POST - Create a new booking
    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody Book book) {
        try {
            Book createdBooking = bookService.createBooking(book);
            return new ResponseEntity<>(createdBooking, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // PUT - Update a booking
    @PutMapping(path = "{idReservation}")
    public ResponseEntity<?> updateBooking(
            @PathVariable Long idReservation,
            @RequestBody Book bookUpdate
    ) {
        try {
            bookService.updateBooking(idReservation, bookUpdate);
            return new ResponseEntity<>("Booking updated successfully", HttpStatus.OK);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // PUT - Cancel a booking
    @PutMapping(path = "{idReservation}/cancel")
    public ResponseEntity<?> cancelBooking(@PathVariable Long idReservation) {
        try {
            bookService.cancelBooking(idReservation);
            return new ResponseEntity<>("Booking cancelled successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // DELETE - Delete a booking
    @DeleteMapping(path = "{idReservation}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long idReservation) {
        if (bookService.getBookingById(idReservation).isPresent()) {
            bookService.deleteBooking(idReservation);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
