package com.masikga.itwc.domain.worldcup.repository.projection;

public record GetDividedWorldCupGameContentsProjection(
	long contentsId,
	String name,
	long mediaFileId,
	String FileType,
	String movieStartTime,
	Integer moviePlayDuration
) {
}