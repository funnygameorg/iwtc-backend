package com.example.demo.domain.worldcup.controller.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record GetWorldCupContentsResponse (
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
    ) { }
}
