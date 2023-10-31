package com.example.demo.domain.worldcup.repository.projection;


import java.util.List;

public record GetDividedWorldCupGameContentsProjection(
        long contentsId,
        String name,
        String absoluteName,
        String filePath
) {}
