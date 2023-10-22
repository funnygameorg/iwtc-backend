package com.example.demo.worldcup.service;

import com.example.demo.worldcup.controller.vo.WorldCupDateRange;
import com.example.demo.worldcup.model.WorldCupGameRepository;
import com.example.demo.worldcup.model.entity.WorldCupGame;
import com.example.demo.worldcup.model.projection.FindWorldCupGamePageProjection;
import com.example.demo.worldcup.repository.WorldCupGameJpaRepository;
import com.example.demo.worldcup.service.dto.WorldCupResponse;
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
        LocalDate startDate = LocalDate.now().minusDays(3);
        LocalDate endDate = LocalDate.now();
        return worldCupGameRepository.findWorldCupGamePage(
                startDate,
                endDate,
                pageable
        );
    }
}
