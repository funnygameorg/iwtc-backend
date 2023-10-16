package com.example.demo.member.service.dto;

import lombok.Builder;

@Builder
public record SignInResponse(
        String accessToken,
        String refreshToken
) { }
