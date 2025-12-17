package com.epita.airlineapi.controller;

import com.epita.airlineapi.model.Client;
import com.epita.airlineapi.model.MilesReward;
import com.epita.airlineapi.repository.ClientRepository;
import com.epita.airlineapi.service.MilesRewardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/v1/miles-reward")
public class MilesRewardController {

    private final MilesRewardService milesRewardService;
    private final ClientRepository clientRepository;

    public MilesRewardController(MilesRewardService milesRewardService, ClientRepository clientRepository) {
        this.milesRewardService = milesRewardService;
        this.clientRepository = clientRepository;
    }

    @GetMapping
    public ResponseEntity<?> getAllRewards() {
        List<MilesReward> rewards = milesRewardService.getAllRewards();
        if (rewards.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(rewards, HttpStatus.OK);
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<?> getRewardById(@PathVariable Long id) {
        Optional<MilesReward> reward = milesRewardService.getRewardById(id);
        return reward.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping(path = "/client/{passportNumber}")
    public ResponseEntity<?> getRewardsByClient(@PathVariable String passportNumber) {
        Optional<Client> client = clientRepository.findById(passportNumber);
        if (client.isEmpty()) {
            return new ResponseEntity<>("Client not found", HttpStatus.NOT_FOUND);
        }

        List<MilesReward> rewards = milesRewardService.getRewardsByClient(client.get());
        if (rewards.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(rewards, HttpStatus.OK);
    }

    @GetMapping(path = "/client/{passportNumber}/discount")
    public ResponseEntity<?> getDiscountCode(@PathVariable String passportNumber) {
        Optional<Client> client = clientRepository.findById(passportNumber);
        if (client.isEmpty()) {
            return new ResponseEntity<>("Client not found", HttpStatus.NOT_FOUND);
        }

        Optional<String> discountCode = milesRewardService.getDiscountCodeForClient(client.get());

        Map<String, Object> response = new HashMap<>();
        response.put("passportNumber", passportNumber);
        response.put("hasDiscount", discountCode.isPresent());
        response.put("discountCode", discountCode.orElse(null));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/client/{passportNumber}/flight-count")
    public ResponseEntity<?> getFlightCount(@PathVariable String passportNumber) {
        Optional<Client> client = clientRepository.findById(passportNumber);
        if (client.isEmpty()) {
            return new ResponseEntity<>("Client not found", HttpStatus.NOT_FOUND);
        }

        int flightCount = milesRewardService.getFlightCountThisYear(client.get());

        Map<String, Object> response = new HashMap<>();
        response.put("passportNumber", passportNumber);
        response.put("flightsThisYear", flightCount);
        response.put("needsMoreForDiscount", Math.max(0, 3 - flightCount));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}