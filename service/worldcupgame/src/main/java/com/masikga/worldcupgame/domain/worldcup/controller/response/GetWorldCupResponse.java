package com.masikga.worldcupgame.domain.worldcup.controller.response;

import com.masikga.worldcupgame.domain.worldcup.model.WorldCupGame;
import com.masikga.worldcupgame.domain.worldcup.model.vo.VisibleType;

import java.time.LocalDateTime;

public record GetWorldCupResponse(
	Long worldCupId,
	String title,
	String description,
	VisibleType visibleType,
	LocalDateTime createdAt,
	LocalDateTime updatedAt
) {

	public static GetWorldCupResponse fromEntity(WorldCupGame worldCupGame) {
		return new GetWorldCupResponse(
			worldCupGame.getId(),
			worldCupGame.getTitle(),
			worldCupGame.getDescription(),
			worldCupGame.getVisibleType(),
			worldCupGame.getCreatedAt(),
			worldCupGame.getUpdatedAt()
		);
	}
}
