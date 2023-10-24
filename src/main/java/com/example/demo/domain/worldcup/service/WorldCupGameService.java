package com.example.demo.domain.worldcup.service;

import com.example.demo.domain.worldcup.model.projection.FindWorldCupGamePageProjection;
import com.example.demo.domain.worldcup.model.entity.vo.WorldCupDateRange;
import com.example.demo.domain.worldcup.model.repository.WorldCupGameRepository;
import lombok.RequiredArgsConstructor;
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
    public Page<FindWorldCupGamePageProjection> findWorldCupByPageable(
            Pageable pageable,
            WorldCupDateRange dateRange,
            String worldCupKeyword)
    {
        LocalDate now = LocalDate.now();
        LocalDate startDate = calculatePagingStartDate(dateRange, now);

        return worldCupGameRepository.findWorldCupGamePage(
                startDate,
                now,
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
