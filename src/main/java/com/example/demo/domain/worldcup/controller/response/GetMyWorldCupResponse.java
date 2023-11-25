package com.example.demo.domain.worldcup.controller.response;

import com.example.demo.domain.worldcup.model.WorldCupGame;
import com.example.demo.domain.worldcup.model.WorldCupGameContents;

public record GetMyWorldCupResponse(
        Long id,
        String title,
        String description
) {
    public static GetMyWorldCupResponse fromEntity(WorldCupGame worldCupGame) {
        return new GetMyWorldCupResponse(
                worldCupGame.getId(),
                worldCupGame.getTitle(),
                worldCupGame.getDescription()
        );
    }
}
