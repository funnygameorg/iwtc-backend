package com.example.demo.member.controller.dto;

import java.time.LocalDateTime;

public record GetMySummaryResponse (
        Long id,
        String serviceId,
        String nickname
) {}
