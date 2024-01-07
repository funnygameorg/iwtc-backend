package com.masikga.itwc.domain.etc.controller.response;

import java.time.LocalDateTime;

import com.masikga.itwc.domain.etc.component.MediaFileComponent;
import com.masikga.itwc.domain.etc.model.vo.FileType;

public record MediaFileResponse(
	long mediaFileId,
	FileType fileType,
	String mediaData,
	String originalName,
	String videoStartTime,
	Integer videoPlayDuration,
	LocalDateTime createdAt,
	LocalDateTime updatedAt
) {

	public static MediaFileResponse fromEntity(
		MediaFileComponent.AllFieldMediaFileDto mediaFile,
		String mediaFileBody
	) {

		return new MediaFileResponse(
			mediaFile.id(),
			mediaFile.fileType(),
			mediaFileBody,
			mediaFile.originalName(),
			mediaFile.videoStartTime(),
			mediaFile.videoPlayDuration(),
			mediaFile.createdAt(),
			mediaFile.updatedAt()
		);
	}

}
