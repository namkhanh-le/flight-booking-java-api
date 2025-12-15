package com.epita.airlineapi.repository;

import com.epita.airlineapi.model.Book;
import com.epita.airlineapi.model.Client;
import com.epita.airlineapi.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    // Find all bookings for a specific client
    List<Book> findByClient(Client client);

    // Find all bookings for a specific flight
    List<Book> findByFlight(Flight flight);

    // Count bookings for a specific flight (to check seat availability)
    long countByFlight(Flight flight);

    // Find bookings by client passport number
    @Query("SELECT b FROM Book b WHERE b.client.passportNumber = ?1")
    List<Book> findByClientPassportNumber(String passportNumber);
}
