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
    List<PlayContents> contentsList
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
                        .map(PlayContents::fromProjection)
                        .collect(toList())
        );
    }

    @Getter
    public static class PlayContents{
            String fileType;
            Long contentsId;
            String name;
            String filePath;
            String internetMovieStartPlayTime;
            Integer playDuration;

        public static PlayContents fromProjection(GetDividedWorldCupGameContentsProjection projection) {
            PlayContents instance = new PlayContents();
            instance.fileType = projection.FileType();
            instance.contentsId = projection.contentsId();
            instance.name = projection.name();
            instance.filePath = projection.filePath();
            instance.internetMovieStartPlayTime = projection.movieStartTime();
            instance.playDuration = projection.moviePlayDuration();
            return instance;
        }
    }
}
