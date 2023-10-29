package com.example.demo.domain.worldcup.controller.response;

import com.example.demo.domain.worldcup.model.vo.VisibleType;

import java.time.LocalDateTime;

public record GetWorldCupResponse (
        Long worldCupId,
        String title,
        String description,
        VisibleType visibleType,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

}
