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

    private static final String WORLD_CUP_GAME_TABLE_PK= "worldCupGameId";

    @Override
    public Page<GetWorldCupGamePageProjection> getWorldCupGamePage(
            LocalDate startDate,
            LocalDate endDate,
            String worldCupGameKeyword,
            Pageable pageable
    ) {
        return worldCupGamePageRepositoryImpl.getWorldCupGamePage(startDate, endDate, worldCupGameKeyword, pageable);
    }

    @Override
    public Boolean existsWorldCupGame(Long worldCupGameId) {
        String nativeQuery = "SELECT EXISTS (SELECT 1 FROM WORLD_CUP_GAME WHERE id = :worldCupGameId)";
        Query query = em.createNativeQuery(nativeQuery);
        query.setParameter(
                WORLD_CUP_GAME_TABLE_PK,
                worldCupGameId
        );

        return (Boolean) query.getSingleResult();
    }

    @Override
    public GetAvailableGameRoundsProjection getAvailableGameRounds(Long worldCupGameId) {
        String sql = """
                SELECT new com.example.demo.domain.worldcup.repository.projection.GetAvailableGameRoundsProjection(
                    wcg.id, wcg.title, wcg.description, count(wcgc.id) as count
                )
                FROM WorldCupGame wcg 
                LEFT JOIN WorldCupGameContents wcgc 
                ON wcg = wcgc.worldCupGame 
                WHERE wcg.id = :worldCupGameId 
                GROUP BY wcg.id 
                """;
        TypedQuery<GetAvailableGameRoundsProjection> query = em.createQuery(sql, GetAvailableGameRoundsProjection.class);
        query.setParameter(
                WORLD_CUP_GAME_TABLE_PK,
                worldCupGameId
        );
        return query.getSingleResult();
    }

    @Override
    public void incrementView(Long worldCupGameId) {

    }

}
