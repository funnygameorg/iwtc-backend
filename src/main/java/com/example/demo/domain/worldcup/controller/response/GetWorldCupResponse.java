package com.example.demo.domain.worldcup.controller.response;

import java.time.LocalDateTime;

import com.example.demo.domain.worldcup.model.WorldCupGame;
import com.example.demo.domain.worldcup.model.vo.VisibleType;

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
