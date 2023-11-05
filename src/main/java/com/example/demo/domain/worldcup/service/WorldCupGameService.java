package com.example.demo.domain.worldcup.service;

import com.example.demo.domain.worldcup.repository.projection.GetWorldCupGamePageProjection;
import com.example.demo.domain.worldcup.controller.vo.WorldCupDateRange;
import com.example.demo.domain.worldcup.repository.WorldCupGameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WorldCupGameService {

    private final WorldCupGameRepository worldCupGameRepository;

    @Cacheable(
            value = "findWorldCupByPageable",
            key = "#dateRange?.toString() + #worldCupKeyword?.toString() + #pageable?.toString()",
            unless = "#result.isEmpty()"
    )
    public Page<GetWorldCupGamePageProjection> findWorldCupByPageable(
            Pageable pageable,
            WorldCupDateRange dateRange,
            String worldCupKeyword)
    {
        LocalDate now = LocalDate.now();
        LocalDate startDate = calculatePagingStartDate(dateRange, now);

        return worldCupGameRepository.getWorldCupGamePage(
                startDate,
                now,
                worldCupKeyword,
                pageable
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
