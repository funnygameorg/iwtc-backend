package com.masikga.itwc.domain.worldcup.repository.impl.mapper;

import com.masikga.itwc.domain.worldcup.repository.projection.GetAvailableGameRoundsProjection;
import com.masikga.itwc.domain.worldcup.repository.projection.GetDividedWorldCupGameContentsProjection;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface WorldCupGameMapper {

    Boolean existsWorldCupGame(Long worldCupGameId);

    GetAvailableGameRoundsProjection getAvailableGameRounds(Long worldCupGameId);

    List<GetDividedWorldCupGameContentsProjection> getDividedWorldCupGameContents(
            @Param("worldCupGameId") Long worldCupId,
            @Param("divideContentsSizePerRequest") Integer wantedContentsSize,
            @Param("alreadyPlayedContentsIds") List<Long> alreadyPlayedContentsIds
    );

}
