package com.epita.airlineapi.service;

import com.epita.airlineapi.model.Client;
import com.epita.airlineapi.repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    // 🔹 Get all clients
    public List<Client> getClients() {
        return clientRepository.findAll();
    }

    // 🔹 Create a new client
    public Client saveClient(Client client) {
        return clientRepository.save(client);
    }

    // 🔹 Delete client by passport number
    public void deleteClient(String passportNumber) {
        if (!clientRepository.existsById(passportNumber)) {
            throw new IllegalStateException("Client not found");
        }
        clientRepository.deleteById(passportNumber);
    }
}
