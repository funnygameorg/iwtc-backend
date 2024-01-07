package com.masikga.itwc.domain.worldcup.service;

import java.time.LocalDate;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.masikga.itwc.domain.worldcup.controller.vo.WorldCupDateRange;
import com.masikga.itwc.domain.worldcup.repository.WorldCupGameRepository;
import com.masikga.itwc.domain.worldcup.repository.projection.GetWorldCupGamePageProjection;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WorldCupGameService {

	private final WorldCupGameRepository worldCupGameRepository;

	// 기본 인자 + 첫 페이지 조회 조건으로만 cache를 사용한다.
	@Cacheable(
		cacheNames = "worldCupList",
		key = "'firstPage'",
		condition = "#pageable.getPageNumber() == 0 "
			+ "and #worldCupKeyword == null "
			+ "and #memberId == null "
			+ "and #pageable.getSort().toString() == 'id: DESC' "
			+ "and #dateRange.name() == 'ALL' "
	)
	public Page<GetWorldCupGamePageProjection> findWorldCupByPageable(
		Pageable pageable,
		WorldCupDateRange dateRange,
		String worldCupKeyword,
		Long memberId
	) {

		LocalDate now = LocalDate.now();

		LocalDate startDate = calculatePagingStartDate(dateRange, now);

		return worldCupGameRepository.getWorldCupGamePage(
			startDate,
			now,
			worldCupKeyword,
			pageable,
			memberId
		);
	}

	private LocalDate calculatePagingStartDate(WorldCupDateRange dateRange, LocalDate today) {
		return switch (dateRange) {
			case ALL -> today.minusYears(3);
			case YEAR -> today.minusYears(1);
			case MONTH -> today.minusMonths(1);
			case DAY -> today.minusDays(1);
		};
	}
}
