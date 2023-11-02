package com.example.demo.domain.worldcup.controller.response;

import com.example.demo.domain.worldcup.model.WorldCupGame;
import com.example.demo.domain.worldcup.repository.projection.GetDividedWorldCupGameContentsProjection;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public record GetWorldCupPlayContentsResponse (
    Long worldCupId,
    String title,
    Integer round,
    List<PlayImageContents> contentsList
) {
    public static GetWorldCupPlayContentsResponse fromProjection(
            Long worldCupGameId,
            String WorldCupGameTitle,
            int worldCupGameRound,
            List<GetDividedWorldCupGameContentsProjection> gameContentsProjections
    ) {
        return new GetWorldCupPlayContentsResponse(
                worldCupGameId,
                WorldCupGameTitle,
                worldCupGameRound,
                gameContentsProjections
                        .stream()
                        .map(PlayImageContents::fromProjection)
                        .collect(toList())
        );
    }

    @Getter
    public static class PlayImageContents{
            Long contentsId;
            String name;
            String absoluteName;
            String filePath;

        public static PlayImageContents fromProjection(GetDividedWorldCupGameContentsProjection projection) {
            PlayImageContents instance = new PlayImageContents();
            instance.contentsId = projection.contentsId();
            instance.name = projection.name();
            instance.absoluteName = projection.absoluteName();
            instance.filePath = projection.filePath();
            return instance;
        }
    }
}
