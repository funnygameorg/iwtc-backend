package com.example.demo.domain.worldcup.repository;

import com.example.demo.domain.worldcup.repository.projection.GetAvailableGameRoundsProjection;
import com.example.demo.domain.worldcup.repository.projection.GetWorldCupGamePageProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;

import java.time.LocalDate;

public interface WorldCupGameCustomRepository {

    Page<GetWorldCupGamePageProjection> getWorldCupGamePage(
            LocalDate startDate,
            LocalDate endDate,
            String worldCupGameKeyword,
            Pageable pageable
    );

    Boolean existsWorldCupGame(Long worldCupGameId);
    GetAvailableGameRoundsProjection getAvailableGameRounds(Long worldCupGameId);
    @Async
    void incrementWorldCupGameViews(Long worldCupGameId);
}
