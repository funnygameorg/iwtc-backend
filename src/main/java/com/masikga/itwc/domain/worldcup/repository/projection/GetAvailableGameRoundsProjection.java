package com.masikga.itwc.domain.worldcup.repository.projection;

public record GetAvailableGameRoundsProjection(
	Long worldCupId,
	String worldCupTitle,
	String worldCupDescription,
	Long totalContentsSize
) {
}
