package com.example.demo.domain.worldcup.repository.impl;

import com.example.demo.common.web.auth.rememberme.impl.RedisRepository;
import com.example.demo.domain.worldcup.repository.projection.GetAvailableGameRoundsProjection;
import com.example.demo.domain.worldcup.repository.projection.GetWorldCupGamePageProjection;
import com.example.demo.domain.worldcup.repository.WorldCupGameCustomRepository;
import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sound.midi.SysexMessage;
import java.time.LocalDate;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WorldCupGameRepositoryImpl implements WorldCupGameCustomRepository {

    private final WorldCupGamePageRepositoryImpl worldCupGamePageRepositoryImpl;
    private final RedisTemplate redisTemplate;
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

    /**
     * 월드컵 게임의 조회수를 Redis에서 Increase 연산
     * @param worldCupGameId 조회수 상승하는 게임 id
     */
    @Override
    public void incrementWorldCupGameViews(Long worldCupGameId) {
        ValueOperations ops = redisTemplate.opsForValue();
        String redisViewsKey = "wcg_views_%s".formatted(worldCupGameId);
        ops.increment(redisViewsKey);
    }

}
