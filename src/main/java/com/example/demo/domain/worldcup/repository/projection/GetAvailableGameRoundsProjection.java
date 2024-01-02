package com.example.demo.domain.worldcup.repository.projection;

public record GetAvailableGameRoundsProjection(
	Long worldCupId,
	String worldCupTitle,
	String worldCupDescription,
	Long totalContentsSize
) {
}
