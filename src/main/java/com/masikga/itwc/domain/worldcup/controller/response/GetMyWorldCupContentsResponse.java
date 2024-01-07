package com.masikga.itwc.domain.worldcup.controller.response;

import com.masikga.itwc.domain.worldcup.model.WorldCupGameContents;
import com.masikga.itwc.domain.worldcup.model.vo.VisibleType;

public record GetMyWorldCupContentsResponse(
	Long id,
	String contentsName,
	Long mediaFileId,
	VisibleType visibleType,
	int gameRank,
	int gameScore
) {
	public static GetMyWorldCupContentsResponse fromEntity(WorldCupGameContents worldCupGameContents) {
		return new GetMyWorldCupContentsResponse(
			worldCupGameContents.getId(),
			worldCupGameContents.getName(),
			worldCupGameContents.getMediaFile().getId(),
			worldCupGameContents.getVisibleType(),
			worldCupGameContents.getGameRank(),
			worldCupGameContents.getGameScore()
		);
	}
}
