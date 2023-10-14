package com.example.demo.idealtype.controller.dto.response;

import java.time.LocalDateTime;

public record GetMyWorldCupSummariesResponse (
        Long worldCupId,
        String title,
        Long mediaFile,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) { }
