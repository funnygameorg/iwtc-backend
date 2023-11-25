package com.example.demo.domain.etc.controller.response;

import com.example.demo.domain.etc.component.MediaFileComponent;
import com.example.demo.domain.etc.model.vo.FileType;

import java.time.LocalDateTime;

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
