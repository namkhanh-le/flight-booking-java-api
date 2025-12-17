package com.epita.airlineapi.service;

import com.epita.airlineapi.model.Client;
import com.epita.airlineapi.model.Flight;
import com.epita.airlineapi.model.MilesReward;
import com.epita.airlineapi.repository.MilesRewardRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class MilesRewardService {

    private final MilesRewardRepository milesRewardRepository;

    public MilesRewardService(MilesRewardRepository milesRewardRepository) {
        this.milesRewardRepository = milesRewardRepository;
    }

    public List<MilesReward> getAllRewards() {
        return milesRewardRepository.findAll();
    }

    public Optional<MilesReward> getRewardById(Long id) {
        return milesRewardRepository.findById(id);
    }

    public List<MilesReward> getRewardsByClient(Client client) {
        return milesRewardRepository.findByClient(client);
    }

    public Optional<String> getDiscountCodeForClient(Client client) {
        return milesRewardRepository.findDiscountCodeByClient(client)
                .map(MilesReward::getDiscountCode);
    }

    public MilesReward recordFlight(Client client, Flight flight) {
        MilesReward reward = new MilesReward();
        reward.setClient(client);
        reward.setFlight(flight);
        reward.setDate(LocalDate.now());

        MilesReward savedReward = milesRewardRepository.save(reward);

        checkAndGenerateDiscount(client);

        return savedReward;
    }

    private void checkAndGenerateDiscount(Client client) {
        int currentYear = LocalDate.now().getYear();
        List<MilesReward> rewardsThisYear = milesRewardRepository.findByClientAndYear(client, currentYear);

        if (rewardsThisYear.size() >= 3) {
            Optional<MilesReward> existingDiscount = milesRewardRepository.findDiscountCodeByClient(client);

            if (existingDiscount.isEmpty()) {
                String discountCode = generateDiscountCode();

                MilesReward latestReward = rewardsThisYear.get(rewardsThisYear.size() - 1);
                latestReward.setDiscountCode(discountCode);
                milesRewardRepository.save(latestReward);
            }
        }
    }

    private String generateDiscountCode() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder code = new StringBuilder("SAVE2025");

        for (int i = 0; i < 6; i++) {
            code.append(chars.charAt(random.nextInt(chars.length())));
        }

        return code.toString();
    }

    public int getFlightCountThisYear(Client client) {
        int currentYear = LocalDate.now().getYear();
        return milesRewardRepository.findByClientAndYear(client, currentYear).size();
    }
}