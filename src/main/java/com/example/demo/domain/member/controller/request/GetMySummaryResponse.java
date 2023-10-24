package com.example.demo.domain.member.controller.request;

public record GetMySummaryResponse (
        Long id,
        String serviceId,
        String nickname
) {}
