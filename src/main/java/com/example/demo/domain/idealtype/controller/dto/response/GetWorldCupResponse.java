package com.example.demo.domain.idealtype.controller.dto.response;

import com.example.demo.domain.idealtype.model.vo.VisibleType;
import org.springframework.cglib.core.Local;

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
