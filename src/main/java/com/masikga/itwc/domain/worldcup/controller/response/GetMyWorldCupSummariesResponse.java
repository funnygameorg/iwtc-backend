package com.masikga.itwc.domain.worldcup.controller.response;

import java.time.LocalDateTime;

public record GetMyWorldCupSummariesResponse(
	Long worldCupId,
	String title,
	Long mediaFile,
	LocalDateTime createdAt,
	LocalDateTime updatedAt
) {
}
