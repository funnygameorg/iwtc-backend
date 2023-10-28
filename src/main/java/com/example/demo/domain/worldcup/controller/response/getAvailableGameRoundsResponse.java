package com.example.demo.domain.worldcup.controller.response;

import java.util.List;

public record getAvailableGameRoundsResponse (
        Long worldCupId,
        String worldCupTitle,
        String worldCupDescription,
        List<Integer> rounds
) {}