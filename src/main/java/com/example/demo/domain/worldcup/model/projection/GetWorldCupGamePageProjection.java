package com.example.demo.domain.worldcup.model.projection;

public record GetWorldCupGamePageProjection(
        Long id,
        String title,
        String description,
        String contentsName1,
        String filePath1,
        String contentsName2,
        String filePath2
) {}
