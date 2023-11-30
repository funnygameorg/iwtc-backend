package com.example.demo.domain.worldcup.controller.response;

import com.example.demo.domain.worldcup.model.WorldCupGameContents;
import com.example.demo.domain.worldcup.model.vo.VisibleType;
import lombok.Builder;

@Builder
public record GetGameResultContentsListResponse(

        Long id,

        String contentsName,

        Long mediaFileId,

        int gameRank,

        int gameScore

) {

    public static GetGameResultContentsListResponse fromEntity(WorldCupGameContents worldCupGameContents) {

        return GetGameResultContentsListResponse.builder()
                .id(worldCupGameContents.getId())
                .contentsName(worldCupGameContents.getName())
                .mediaFileId(worldCupGameContents.getMediaFile().getId())
                .gameRank(worldCupGameContents.getGameRank())
                .gameScore(worldCupGameContents.getGameScore())
                .build();

    }
}
