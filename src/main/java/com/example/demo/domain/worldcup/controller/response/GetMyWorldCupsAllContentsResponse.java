package com.example.demo.domain.worldcup.controller.response;

public record GetMyWorldCupsAllContentsResponse (
    Long contentId,
    String filePath,
    String absoluteName,
    String extension
) {

}