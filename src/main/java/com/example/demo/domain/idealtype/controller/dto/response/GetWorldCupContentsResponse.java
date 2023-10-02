package com.example.demo.domain.idealtype.controller.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record GetWorldCupContentsResponse (
        Long contentsId,
        String contentsName,
        BigDecimal winningChange,
        Integer rank,
        Long mediaFileId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

}
