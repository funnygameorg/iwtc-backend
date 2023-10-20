package com.example.demo.worldcup.controller.dto.response;

import java.time.LocalDateTime;

public record GetMyWorldCupSummariesResponse (
        Long worldCupId,
        String title,
        Long mediaFile,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) { }
