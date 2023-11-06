package com.example.demo.domain.worldcup.repository.impl;

import com.example.demo.common.web.auth.rememberme.impl.RedisRepository;
import com.example.demo.domain.worldcup.repository.projection.GetAvailableGameRoundsProjection;
import com.example.demo.domain.worldcup.repository.projection.GetDividedWorldCupGameContentsProjection;
import com.example.demo.domain.worldcup.repository.projection.GetWorldCupGamePageProjection;
import com.example.demo.domain.worldcup.repository.WorldCupGameCustomRepository;
import com.google.common.collect.Iterables;
import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.sound.midi.SysexMessage;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WorldCupGameRepositoryImpl implements WorldCupGameCustomRepository {

    private final WorldCupGamePageRepositoryImpl worldCupGamePageRepositoryImpl;
    private final RedisTemplate redisTemplate;
    private final EntityManager em;

    private static final String WORLD_CUP_GAME_TABLE_PK= "worldCupGameId";
    private static final String INCREMENT_GAME_VIEWS_KEY_FORMAT = "'type':'gameView', 'game':%s";
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
        String redisViewsKey = INCREMENT_GAME_VIEWS_KEY_FORMAT.formatted(worldCupGameId);
        ops.increment(redisViewsKey);
    }


    /**
     * 게임 플레이에 사용되는 이상형 리스트 조회
     * @param worldCupId 조회하는 월드컵 게임 Id
     * @param wantedContentsSize 조회해야 하는 컨텐츠 수
     * @param alreadyPlayedContentsIds 제외하는 컨텐츠 아이디
     * @return
     */
    @Override
    public List<GetDividedWorldCupGameContentsProjection> getDividedWorldCupGameContents(
            Long worldCupId,
            int wantedContentsSize,
            List<Long> alreadyPlayedContentsIds
    ) {
        String NotInAlreadyPlayedContentsIdsDynamicQuery = buildAlreadyPlayedContentsIdsCondition(alreadyPlayedContentsIds);
        String sql = """
                    SELECT 
                        wcgc.ID AS ID, 
                        wcgc.NAME AS CONTENTS_NAME, 
                        mf.ABSOLUTE_NAME AS FILE_ABSOLUTE_NAME, 
                        mf.FILE_PATH AS FILE_PATH
                    FROM (
                        SELECT 
                            inner_wcgc.ID AS id, 
                            inner_wcgc.NAME AS name, 
                            inner_wcgc.MEDIA_FILE_ID AS MEDIA_FILE_ID
                        FROM 
                            WORLD_CUP_GAME_CONTENTS AS INNER_WCGC
                        WHERE 
                            inner_wcgc.WORLD_CUP_GAME_ID = :worldCupGameId
                        %s
                    ) AS wcgc
                    LEFT JOIN 
                        MEDIA_FILE AS mf ON mf.ID = wcgc.MEDIA_FILE_ID
                    LIMIT :divideContentsSizePerRequest
                """
                .formatted(NotInAlreadyPlayedContentsIdsDynamicQuery);

        Query query = em.createNativeQuery(sql, Tuple.class)
                .setParameter(WORLD_CUP_GAME_TABLE_PK, worldCupId)
                .setParameter("divideContentsSizePerRequest", wantedContentsSize);

        List<Tuple> result = query.getResultList();

        return result.stream()
                .map(tuple ->
                        new GetDividedWorldCupGameContentsProjection(
                                Long.parseLong(String.valueOf(tuple.get("ID"))),
                                (String) tuple.get("CONTENTS_NAME"),
                                (String) tuple.get("FILE_ABSOLUTE_NAME"),
                                (String) tuple.get("FILE_PATH")
                        )
                ).collect(Collectors.toList());
    }

    private String buildAlreadyPlayedContentsIdsCondition(List<Long> alreadyPlayedContentsIds) {
        if(CollectionUtils.isEmpty(alreadyPlayedContentsIds))
            return "";
        else
            return " AND inner_wcgc.ID NOT IN (%s)  "
                    .formatted(alreadyPlayedContentsIds.toString())
                    .replace("[", "")
                    .replace("]", "");
    }

}
