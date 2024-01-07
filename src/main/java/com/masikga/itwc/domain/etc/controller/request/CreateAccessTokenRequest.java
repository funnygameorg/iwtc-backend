package com.masikga.itwc.domain.etc.controller.request;

public record CreateAccessTokenRequest(
	String accessToken,
	String refreshToken
) {
}