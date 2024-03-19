package com.masikga.worldcupgame.domain.worldcup.controller.response;

import com.masikga.worldcupgame.domain.worldcup.model.WorldCupGame;

public record GetMyWorldCupResponse(
	Long worldCupId,
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
