package com.example.demo.worldcup.controller.dto.response;

import java.util.List;

public record GetWorldCupPlayContentsResponse (
    Long worldCupId,
    String title,
    Integer round,
    List<PlayImageContents> contentsList
) {
    public record PlayImageContents(
            Long contentsId,
            String name,
            String absoluteName,
            String filePath,
            String extension
    ){}
}
