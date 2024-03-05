package com.masikga.itwc.domain.member.controller.response;

import lombok.Builder;

@Builder
public record SignInResponse(
	String accessToken,
	String refreshToken
) {
}
