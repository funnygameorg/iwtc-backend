package com.masikga.itwc.domain.worldcup.repository.projection;

import org.apache.ibatis.type.Alias;

@Alias("GetDividedWorldCupGameContentsProjection")
public record GetDividedWorldCupGameContentsProjection(
        long contentsId,
        String name,
        long mediaFileId,
        String FileType,
        String movieStartTime,
        Integer moviePlayDuration
) {
}