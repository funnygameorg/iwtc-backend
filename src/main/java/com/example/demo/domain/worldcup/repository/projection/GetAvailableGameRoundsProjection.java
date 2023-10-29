package com.example.demo.domain.worldcup.repository.projection;

import java.util.List;

public record GetAvailableGameRoundsProjection(
        Long worldCupId,
        String worldCupTitle,
        String worldCupDescription,
        Long totalContentsSize
) {
}
