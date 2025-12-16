package com.epita.airlineapi.controller;

import com.epita.airlineapi.model.Client;
import com.epita.airlineapi.service.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/client")
@CrossOrigin(origins = "*") // allows frontend access
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    // 🔹 GET all clients
    @GetMapping
    public ResponseEntity<List<Client>> getClients() {
        List<Client> clients = clientService.getClients();

        if (clients.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(clients, HttpStatus.OK);
    }

    // 🔹 CREATE a new client
    @PostMapping
    public ResponseEntity<Client> createClient(@RequestBody Client client) {
        Client createdClient = clientService.saveClient(client);
        return new ResponseEntity<>(createdClient, HttpStatus.CREATED);
    }

    // 🔹 DELETE client by passport number
    @DeleteMapping("{passportNumber}")
    public ResponseEntity<Void> deleteClient(@PathVariable String passportNumber) {
        clientService.deleteClient(passportNumber);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
