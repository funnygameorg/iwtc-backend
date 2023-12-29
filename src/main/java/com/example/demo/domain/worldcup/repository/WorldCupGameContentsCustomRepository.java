package com.example.demo.domain.worldcup.repository;

import org.springframework.scheduling.annotation.Async;

public interface WorldCupGameContentsCustomRepository {

    // TODO : 인프라 구현체가 자주 변하는 기능
    void saveWinnerContentsScore(
            long worldCupGameId,
            long WorldCupGameContentsId,
            long score
    );

}
