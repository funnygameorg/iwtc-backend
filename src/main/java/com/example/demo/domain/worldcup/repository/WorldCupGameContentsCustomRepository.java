package com.example.demo.domain.worldcup.repository;

import org.springframework.scheduling.annotation.Async;

public interface WorldCupGameContentsCustomRepository {
    void saveWinnerContentsScore(
            long worldCupGameId,
            long WorldCupGameContentsId,
            long score
    );

}
