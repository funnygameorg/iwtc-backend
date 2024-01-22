package com.masikga.itwc.domain.worldcup.controller.response;

import com.masikga.itwc.domain.worldcup.model.WorldCupGameContents;

import lombok.Builder;

@Builder
public record GetGameResultContentsListResponse(

	Long contentsId,

	String contentsName,

	Long mediaFileId,

	int gameRank,

	int gameScore

) {

	public static GetGameResultContentsListResponse fromEntity(WorldCupGameContents worldCupGameContents,
		Integer rank) {

		return GetGameResultContentsListResponse.builder()
			.contentsId(worldCupGameContents.getId())
			.contentsName(worldCupGameContents.getName())
			.mediaFileId(worldCupGameContents.getMediaFile().getId())
			.gameRank(rank)
			.gameScore(worldCupGameContents.getGameScore())
			.build();

	}
}
