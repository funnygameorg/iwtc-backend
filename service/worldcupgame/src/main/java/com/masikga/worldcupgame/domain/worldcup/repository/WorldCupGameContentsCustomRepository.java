package com.masikga.worldcupgame.domain.worldcup.repository;

public interface WorldCupGameContentsCustomRepository {

    // TODO : 인프라 구현체가 자주 변하는 기능
    void saveWinnerContentsScore(
            long worldCupGameId,
            long WorldCupGameContentsId,
            long score
    );

    void saveWinnerContentsScoreV2(
            long WorldCupGameContentsId,
            long score
    );

}