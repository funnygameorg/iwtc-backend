package com.masikga.itwc.domain.worldcup.repository.projection;

import org.apache.ibatis.type.Alias;

@Alias("GetAvailableGameRoundsProjection")
public record GetAvailableGameRoundsProjection(
        Long worldCupId,
        String worldCupTitle,
        String worldCupDescription,
        Long totalContentsSize
) {
}
