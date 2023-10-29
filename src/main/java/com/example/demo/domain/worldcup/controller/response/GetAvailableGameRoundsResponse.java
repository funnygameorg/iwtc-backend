package com.example.demo.domain.worldcup.controller.response;

import java.util.List;

public record GetAvailableGameRoundsResponse(
        Long worldCupId,
        String worldCupTitle,
        String worldCupDescription,
        List<Integer> rounds
) {}