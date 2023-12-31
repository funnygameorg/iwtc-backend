package com.masikga.itwc.domain.worldcup.controller.response;

import java.time.LocalDateTime;

import com.masikga.itwc.domain.worldcup.model.WorldCupGame;
import com.masikga.itwc.domain.worldcup.model.vo.VisibleType;

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
