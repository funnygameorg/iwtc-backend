package com.example.demo.domain.worldcup.controller.dto.response;

import org.springframework.data.util.Pair;

public record GetWorldCupsResponse (
        String gameTitle,
        String gameDescription,
        Pair<IdealImage, IdealImage> idealImagePair
) {
    record IdealImage(
            String contentsName,
            String filePath
    ) {}
}
