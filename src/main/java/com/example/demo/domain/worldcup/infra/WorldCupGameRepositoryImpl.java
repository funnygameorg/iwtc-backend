package com.example.demo.domain.worldcup.infra;

import com.example.demo.domain.worldcup.model.projection.GetWorldCupGamePageProjection;
import com.example.demo.domain.worldcup.model.repository.WorldCupGameQueryRepository;
import com.google.common.base.CharMatcher;
import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static org.springframework.util.StringUtils.hasText;
import static org.springframework.util.StringUtils.trimAllWhitespace;


@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WorldCupGameRepositoryImpl implements WorldCupGameQueryRepository {
    private final EntityManager em;

    public Page<GetWorldCupGamePageProjection> getWorldCupGamePage(
            LocalDate startDate,
            LocalDate endDate,
            String worldCupGameKeyword,
            Pageable pageable
    ) {
        Sort.Order order = getOrderInstance(pageable);
        String likeKeywordCondition = buildWorldCupGameKeywordCondition(worldCupGameKeyword);
        String sortCondition = buildSortCondition(order);

        String sql = getWorldCupGamePagingQuery(likeKeywordCondition, sortCondition);

        List result = em.createNativeQuery(sql, "FindWorldCupGamePageProjectionMapping")
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .setParameter("pageSize", pageable.getPageSize())
                .setParameter("offset", pageable.getOffset())
                .getResultList();

        long total = countByCreatedAtBetween(startDate, endDate);

        return new PageImpl<>(result, pageable, total);
    }

    // 검색 키워드를 제공하면 쿼리문에 대치할 수 있는 조건문을 반환
    public String buildWorldCupGameKeywordCondition(String worldCupGameKeyword) {
        String likeKeywordDynamicQuery = "";
        if(hasText(worldCupGameKeyword)) {
            String removedWhiteKeyword = trimAllWhitespace(worldCupGameKeyword);
            likeKeywordDynamicQuery = "AND CONCAT(wcg.title, wcg.description) LIKE '%" + removedWhiteKeyword + "%' ";
        }
        return likeKeywordDynamicQuery;
    }

    // Order 객체를 제공하면 쿼리문에 대치할 수 있는 조건문을 반환
    private String buildSortCondition(Sort.Order order) {
        String orderBy = "id";
        String orderDirection = order.getDirection().toString();

        if(hasText(order.getProperty())) {
            orderBy = order.getProperty();
        }

        return "ORDER BY wcg.%s %s".formatted(orderBy, orderDirection);
    }

    private Sort.Order getOrderInstance(Pageable pageable) {
        return pageable.getSort().iterator().next();
    }

    private long countByCreatedAtBetween(LocalDate startDate, LocalDate endDate) {
        String sql = """
                SELECT COUNT(*) FROM world_cup_game WHERE DATE(created_at) BETWEEN :startDate AND :endDate
                """;
        Query query = em.createNativeQuery(sql);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        return ((Number) query.getSingleResult()).longValue();
    }
    private String getWorldCupGamePagingQuery(String... condition) {
        return """
            SELECT wcg.id AS id, wcg.title AS title, wcg.description AS description, 
            wcgc_max.name AS contentsName1, mf_max.file_path AS filePath1, 
            wcgc_min.name AS contentsName2, mf_min.file_path AS filePath2 
            FROM world_cup_game AS wcg 
            INNER JOIN world_cup_game_contents AS wcgc_max ON wcgc_max.id = (
                SELECT MAX(inner_wcgc.id) AS id 
                FROM world_cup_game_contents AS inner_wcgc 
                WHERE inner_wcgc.world_cup_game_id = wcg.id 
                AND inner_wcgc.id IN (
                    SELECT inner_wcgc_2.id 
                    FROM world_cup_game_contents AS inner_wcgc_2 
                    WHERE inner_wcgc_2.world_cup_game_id = wcg.id 
                    ORDER BY inner_wcgc_2.id DESC 
                    LIMIT 2
                )
            ) 
            INNER JOIN world_cup_game_contents AS wcgc_min ON wcgc_min.id = (
                SELECT MIN(inner_wcgc.id) AS id 
                FROM world_cup_game_contents AS inner_wcgc 
                WHERE inner_wcgc.world_cup_game_id = wcg.id 
                AND inner_wcgc.id IN (
                    SELECT inner_wcgc_2.id 
                    FROM world_cup_game_contents AS inner_wcgc_2 
                    WHERE inner_wcgc_2.world_cup_game_id = wcg.id 
                    ORDER BY inner_wcgc_2.id DESC 
                    LIMIT 2
                )
            ) 
            INNER JOIN media_file AS mf_max on wcgc_max.media_file_id = mf_max.id 
            INNER JOIN media_file AS mf_min on wcgc_min.media_file_id = mf_min.id 
            WHERE DATE(wcg.created_at) BETWEEN :startDate AND :endDate 
            %s
            GROUP BY wcg.id 
            %s
            LIMIT :pageSize OFFSET :offset
            """.formatted(condition[0], condition[1]);
    }
}
