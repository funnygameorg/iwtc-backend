package com.example.demo.domain.worldcup.repository.impl;

import static org.springframework.util.StringUtils.*;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.worldcup.model.WorldCupGame;
import com.example.demo.domain.worldcup.model.WorldCupGameContents;
import com.example.demo.domain.worldcup.repository.projection.GetWorldCupGamePageProjection;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WorldCupGamePageRepositoryImpl {

	private final static String WORLD_CUP_GAME_START_DATE = "startDate";
	private final static String WORLD_CUP_GAME_END_DATE = "endDate";
	private final static String PAGING_SIZE = "pageSize";
	private final static String PAGING_OFFSET = "offset";
	private final static String PAGING_DEFAULT_SORT_BY = "id";
	private final EntityManager em;

	public Page<GetWorldCupGamePageProjection> getWorldCupGamePage(
		LocalDate startDate,
		LocalDate endDate,
		String worldCupGameKeyword,
		Pageable pageable,
		Long memberId
	) {
		Sort.Order order = getOrderInstance(pageable);

		String sortCondition = buildSortCondition(order);

		String likeKeywordCondition = buildWorldCupGameKeywordCondition(worldCupGameKeyword);

		String isMemberCondition = buildMemberIdCondition(memberId);

		String sql = getWorldCupGamePagingQuery(likeKeywordCondition, isMemberCondition, sortCondition);

		return new PageImpl<>(
			getWorldCupGamePage(startDate, endDate, pageable, sql),
			pageable,
			countByCreatedAtBetween(startDate, endDate)
		);

	}

	// 메인 게임 컨텐츠 조회 쿼리
	private List<GetWorldCupGamePageProjection> getWorldCupGamePage(LocalDate startDate, LocalDate endDate,
		Pageable pageable, String sql) {

		List<WorldCupGame> worldCupList = em.createNativeQuery(sql, WorldCupGame.class)
			.setParameter(WORLD_CUP_GAME_START_DATE, startDate)
			.setParameter(WORLD_CUP_GAME_END_DATE, endDate)
			.setParameter(PAGING_SIZE, pageable.getPageSize())
			.setParameter(PAGING_OFFSET, pageable.getOffset())
			.getResultList();

		return worldCupList.stream().map(worldCupGame -> {
			List<WorldCupGameContents> contentsList = em.createQuery(
					"SELECT wgc FROM WorldCupGameContents wgc WHERE wgc.worldCupGame = :game ORDER BY wgc.id DESC LIMIT 2",
					WorldCupGameContents.class
				)
				.setParameter("game", worldCupGame)
				.getResultList();

			return getGetWorldCupGamePageProjection(worldCupGame, contentsList);
		}).toList();
	}

	private GetWorldCupGamePageProjection getGetWorldCupGamePageProjection(WorldCupGame worldCupGame,
		List<WorldCupGameContents> contentsList) {

		String rank1ContentsName = contentsList.size() > 0 ? contentsList.get(0).getName() : null;
		String rank2ContentsName = contentsList.size() > 1 ? contentsList.get(1).getName() : null;
		Long rank1ContentsMediaFileId = contentsList.size() > 0 ? contentsList.get(0).getMediaFile().getId() : null;
		Long rank2ContentsMediaFileId = contentsList.size() > 1 ? contentsList.get(1).getMediaFile().getId() : null;

		return new GetWorldCupGamePageProjection(
			worldCupGame.getId(),
			worldCupGame.getTitle(),
			worldCupGame.getDescription(),
			rank1ContentsName,
			rank1ContentsMediaFileId,
			rank2ContentsName,
			rank2ContentsMediaFileId
		);

	}

	// 페이지 카운트 쿼리
	private long countByCreatedAtBetween(LocalDate startDate, LocalDate endDate) {

		String sql = """
			SELECT COUNT(*) FROM world_cup_game WHERE DATE(created_at) BETWEEN :startDate AND :endDate
			""";

		Query query = em.createNativeQuery(sql);

		query.setParameter(WORLD_CUP_GAME_START_DATE, startDate);

		query.setParameter(WORLD_CUP_GAME_END_DATE, endDate);

		return ((Number)query.getSingleResult()).longValue();
	}

	// 검색 키워드를 제공하면 쿼리문에 대치할 수 있는 조건문을 반환
	private String buildMemberIdCondition(Long memberId) {

		String isMemberIdDynamicQuery = "";

		if (memberId != null) {
			isMemberIdDynamicQuery = "AND wcg.member_id = " + memberId;
		}

		return isMemberIdDynamicQuery;
	}

	// 검색 키워드를 제공하면 쿼리문에 대치할 수 있는 조건문을 반환
	private String buildWorldCupGameKeywordCondition(String worldCupGameKeyword) {

		String likeKeywordDynamicQuery = "";

		if (hasText(worldCupGameKeyword)) {
			String removedWhiteKeyword = trimAllWhitespace(worldCupGameKeyword);
			likeKeywordDynamicQuery = "AND CONCAT(wcg.title, wcg.description) LIKE '%" + removedWhiteKeyword + "%' ";
		}

		return likeKeywordDynamicQuery;
	}

	// Order 객체를 제공하면 쿼리문에 대치할 수 있는 조건문을 반환
	private String buildSortCondition(Sort.Order order) {

		String orderBy = PAGING_DEFAULT_SORT_BY;

		String orderDirection = order.getDirection().toString();

		if (hasText(order.getProperty())) {
			orderBy = order.getProperty();
		}

		return "ORDER BY wcg.%s %s".formatted(orderBy, orderDirection);
	}

	private Sort.Order getOrderInstance(Pageable pageable) {

		return pageable.getSort().iterator().next();

	}

	private String getWorldCupGamePagingQuery(String... condition) {
		return """
			SELECT 
			    wcg.id, 
			    wcg.title,
			    wcg.description,
			    wcg.visible_type,
			    wcg.views,
			    wcg.soft_delete,
			    wcg.member_id,
			    wcg.world_cup_game_statistics_id,
			    wcg.created_at,
			    wcg.updated_at 
			FROM 
			    world_cup_game AS wcg                      
			WHERE 
			    DATE(wcg.created_at) BETWEEN :startDate AND :endDate 
			%s
			%s
			GROUP BY 
			    wcg.id 
			%s
			LIMIT :pageSize OFFSET :offset
			""".formatted(condition[0], condition[1], condition[2]);
	}

}