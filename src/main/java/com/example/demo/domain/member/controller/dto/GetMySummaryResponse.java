package com.example.demo.domain.member.controller.dto;

import java.time.LocalDateTime;

public record GetMySummaryResponse (
        Long id,
        String serviceId,
        String nickname
) {}
