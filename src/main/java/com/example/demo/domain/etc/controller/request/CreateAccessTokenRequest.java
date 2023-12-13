package com.example.demo.domain.etc.controller.request;

import org.springframework.web.bind.annotation.RequestBody;


public record CreateAccessTokenRequest (
        String accessToken,
        String refreshToken
) {}