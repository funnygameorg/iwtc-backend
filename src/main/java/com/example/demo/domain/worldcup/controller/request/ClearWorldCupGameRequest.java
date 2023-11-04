package com.example.demo.domain.worldcup.controller.request;

import lombok.Builder;

@Builder
public record ClearWorldCupGameRequest (
        int firstWinnerContentsId,
        int secondWinnerContentsId,
        int thirdWinnerContentsId,
        int fourthWinnerContentsId
) {}