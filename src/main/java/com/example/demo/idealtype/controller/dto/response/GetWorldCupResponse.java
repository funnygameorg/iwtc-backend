package com.example.demo.idealtype.controller.dto.response;

import com.example.demo.idealtype.model.vo.VisibleType;

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
