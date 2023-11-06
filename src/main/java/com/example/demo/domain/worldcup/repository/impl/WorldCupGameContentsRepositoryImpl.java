package com.example.demo.domain.worldcup.repository.impl;

import com.example.demo.domain.worldcup.repository.WorldCupGameContentsCustomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WorldCupGameContentsRepositoryImpl implements WorldCupGameContentsCustomRepository {

    private final RedisTemplate redisTemplate;
    private static final String WINNER_CONTENTS_SCORE_KEY_FORMAT = "'Game':%s,'Contents':%s";

    @Override
    public void saveWinnerContentsScore(
            long worldCupGameId,
            long WorldCupGameContentsId,
            long score
    ) {
        String redisKey = WINNER_CONTENTS_SCORE_KEY_FORMAT.formatted(worldCupGameId, WorldCupGameContentsId);

        redisTemplate.opsForValue().increment(redisKey, score);
    }
}
