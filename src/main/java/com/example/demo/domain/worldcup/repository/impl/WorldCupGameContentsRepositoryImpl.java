package com.example.demo.domain.worldcup.repository.impl;

import com.example.demo.domain.worldcup.repository.WorldCupGameContentsCustomRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WorldCupGameContentsRepositoryImpl implements WorldCupGameContentsCustomRepository {


    private final EntityManager em;



    @Override
    public void saveWinnerContentsScore(
            long worldCupGameId,
            long worldCupGameContentsId,
            long score
    ) {
        em.createQuery("UPDATE WorldCupGameContents w SET w.gameScore = w.gameScore + :score WHERE w.id = :id")
                .setParameter("id", worldCupGameContentsId)
                .setParameter("score", score)
                .executeUpdate();
    }



}
