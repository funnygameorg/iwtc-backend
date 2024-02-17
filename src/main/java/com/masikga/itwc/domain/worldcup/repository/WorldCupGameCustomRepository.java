package com.masikga.itwc.domain.worldcup.repository;

import com.masikga.itwc.domain.worldcup.repository.projection.GetAvailableGameRoundsProjection;
import com.masikga.itwc.domain.worldcup.repository.projection.GetDividedWorldCupGameContentsProjection;
import com.masikga.itwc.domain.worldcup.repository.projection.GetWorldCupGamePageProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface WorldCupGameCustomRepository {

    Page<GetWorldCupGamePageProjection> getWorldCupGamePage(
            LocalDate startDate,
            LocalDate endDate,
            String worldCupGameKeyword,
            Pageable pageable,
            Long memberId
    );

    // TODO : 인프라 구현체가 자주 변하는 기능
    void incrementWorldCupGameViews(Long worldCupGameId);

    Boolean existsWorldCupGameV2(Long worldCupGameId);

    GetAvailableGameRoundsProjection getAvailableGameRoundsV2(Long worldCupGameId);

    List<GetDividedWorldCupGameContentsProjection> getDividedWorldCupGameContentsV2(
            Long worldCupId,
            int wantedContentsSize,
            List<Long> alreadyPlayedContentsIds
    );

    @Deprecated
    Boolean existsWorldCupGame(Long worldCupGameId);

    @Deprecated
    GetAvailableGameRoundsProjection getAvailableGameRounds(Long worldCupGameId);

    @Deprecated
    List<GetDividedWorldCupGameContentsProjection> getDividedWorldCupGameContents(
            Long worldCupId,
            int wantedContentsSize,
            List<Long> alreadyPlayedContentsIds
    );
}
