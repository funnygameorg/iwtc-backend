package com.example.demo.domain.member.service.dto;

import lombok.Builder;

@Builder
public record SignInResponse(
        String accessToken,
        String refreshToken
) { }
