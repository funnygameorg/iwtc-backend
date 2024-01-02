package com.example.demo.domain.worldcup.controller.response;

import java.time.LocalDateTime;

import com.example.demo.domain.etc.model.MediaFile;
import com.example.demo.domain.worldcup.model.WorldCupGameContents;

public record GetWorldCupContentsResponse(
	long contentsId,
	String contentsName,
	int rank,
	int score,
	LocalDateTime createdAt,
	LocalDateTime updatedAt,
	FileResponse fileResponse
) {
	public record FileResponse(
		long mediaFileId,
		String filePath,
		LocalDateTime createdAt,
		LocalDateTime updatedAt
	) {
	}

	public static GetWorldCupContentsResponse fromEntity(WorldCupGameContents contents) {

		MediaFile mediaFile = contents.getMediaFile();

		return new GetWorldCupContentsResponse(
			contents.getId(),
			contents.getName(),
			contents.getGameRank(),
			contents.getGameScore(),
			contents.getCreatedAt(),
			contents.getUpdatedAt(),
			new FileResponse(
				mediaFile.getId(),
				mediaFile.getObjectKey(),
				mediaFile.getCreatedAt(),
				mediaFile.getUpdatedAt()
			)
		);
	}
}
