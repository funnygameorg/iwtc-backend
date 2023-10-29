package com.example.demo.domain.worldcup.repository.impl;

import com.example.demo.domain.worldcup.repository.projection.GetAvailableGameRoundsProjection;
import com.example.demo.domain.worldcup.repository.projection.GetWorldCupGamePageProjection;
import com.example.demo.domain.worldcup.repository.WorldCupGameCustomRepository;
import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WorldCupGameRepositoryImpl implements WorldCupGameCustomRepository {

    private final WorldCupGamePageRepositoryImpl worldCupGamePageRepositoryImpl;
    private final EntityManager em;

    @Override
    public Page<GetWorldCupGamePageProjection> getWorldCupGamePage(
            LocalDate startDate,
            LocalDate endDate,
            String worldCupGameKeyword,
            Pageable pageable
    ) {
        worldCupGamePageRepositoryImpl.getWorldCupGamePage(startDate, endDate, worldCupGameKeyword, pageable);
        return null;
    }

    @Override
    public Boolean existsWorldCupGame(Long worldCupGameId) {
        String sql = "SELECT EXISTS (SELECT 1 FROM WORLD_CUP_GAME WHERE id = :worldCupGameId)";
        TypedQuery<Boolean> query = em.createQuery(sql, Boolean.class);
        query.setParameter("worldCupGameId", worldCupGameId);
        return query.getSingleResult();
    }

    @Override
    public GetAvailableGameRoundsProjection getAvailableGameRounds(Long worldCupGameId) {
        return null;
    }

    @Override
    public void incrementView(Long worldCupGameId) {

    }

}
