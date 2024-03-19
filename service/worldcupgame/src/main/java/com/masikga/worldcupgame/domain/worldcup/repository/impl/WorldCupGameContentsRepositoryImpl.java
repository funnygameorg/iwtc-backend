package com.masikga.worldcupgame.domain.worldcup.repository.impl;

import com.masikga.worldcupgame.domain.worldcup.repository.WorldCupGameContentsCustomRepository;
import com.masikga.worldcupgame.domain.worldcup.repository.impl.mapper.WorldCupGameContentsMapper;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WorldCupGameContentsRepositoryImpl implements WorldCupGameContentsCustomRepository {

    private final EntityManager em;
    private final WorldCupGameContentsMapper worldCupGameContentsMapper;

    @Deprecated
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

    @Override
    public void saveWinnerContentsScoreV2(long worldCupGameContentsId, long score) {
        worldCupGameContentsMapper.saveWinnerContentsScore(worldCupGameContentsId, score);
    }

}
