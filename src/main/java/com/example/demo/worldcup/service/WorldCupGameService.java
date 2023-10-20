package com.example.demo.worldcup.service;

import com.example.demo.worldcup.controller.vo.WorldCupDateRange;
import com.example.demo.worldcup.model.WorldCupGame;
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

    private final WorldCupGameJpaRepository worldCupGameJpaRepository;
    public WorldCupResponse findWorldCupByPageable(
            Pageable pageable,
            WorldCupDateRange dateRange,
            String worldCupKeyword)
    {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now();
        Page<WorldCupGame> worldCupGamePage = worldCupGameJpaRepository.findWorldCupGamePage(
                worldCupKeyword,
                startDate,
                endDate,
                pageable
        );
        return new WorldCupResponse();
    }
}
