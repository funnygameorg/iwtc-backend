package com.masikga.worldcupgame.domain.worldcup.controller.response;

import com.masikga.worldcupgame.domain.worldcup.model.WorldCupGameContents;
import com.masikga.worldcupgame.domain.worldcup.model.vo.VisibleType;

public record GetMyWorldCupContentsResponse(
	Long worldCupId,
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
