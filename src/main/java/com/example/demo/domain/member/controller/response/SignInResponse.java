package com.example.demo.domain.member.controller.response;

import lombok.Builder;

@Builder
public record SignInResponse(
	String accessToken,
	String refreshToken
) {
}
