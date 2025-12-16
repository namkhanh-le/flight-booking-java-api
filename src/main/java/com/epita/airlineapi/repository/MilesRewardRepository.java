package com.epita.airlineapi.repository;

import com.epita.airlineapi.model.Client;
import com.epita.airlineapi.model.MilesReward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MilesRewardRepository extends JpaRepository<MilesReward, Long> {

    List<MilesReward> findByClient(Client client);

    @Query("SELECT m FROM MilesReward m WHERE m.client = ?1 AND YEAR(m.date) = ?2")
    List<MilesReward> findByClientAndYear(Client client, int year);

    @Query("SELECT m FROM MilesReward m WHERE m.client = ?1 AND m.discountCode IS NOT NULL")
    Optional<MilesReward> findDiscountCodeByClient(Client client);

    long countByClientAndDateBetween(Client client, LocalDate startDate, LocalDate endDate);
}
