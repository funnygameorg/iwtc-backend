package com.example.demo.domain.worldcup.controller.dto.response;

public record GetMyWorldCupsAllContentsResponse (
    Long contentId,
    String filePath,
    String absoluteName,
    String extension
) {

}
