package com.example.demo.domain.worldcup.model.vo;

import jakarta.persistence.criteria.CriteriaBuilder;

public enum WorldCupGameRound {
    ROUND_2(2),
    ROUND_4(4),
    ROUND_8(8),
    ROUND_16(16),
    ROUND_32(32),
    ROUND_64(64),
    ROUND_128(128),
    ROUND_256(256);

    public final int roundValue;

    WorldCupGameRound(int roundValue) {
        this.roundValue = roundValue;
    }

    public boolean isAvailableRound(Long contentsSize) {
        return this.roundValue <= contentsSize;
    }
}
