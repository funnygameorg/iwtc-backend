package com.example.demo.worldcup.controller.dto.response;

import com.example.demo.worldcup.model.entity.vo.VisibleType;

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
