package com.example.demo.domain.etc.controller.response;

import java.time.LocalDateTime;

public record MediaFileResponse(
        String fileUploadName,
        String absoluteName,
        String filePath,
        String extension,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

}
