package com.masikga.itwc.domain.worldcup.repository.impl.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface WorldCupGameContentsMapper {
    void saveWinnerContentsScore(
            @Param("worldCupGameContentsId") Long worldCupGameContentsId,
            @Param("score") Long score
    );
}
