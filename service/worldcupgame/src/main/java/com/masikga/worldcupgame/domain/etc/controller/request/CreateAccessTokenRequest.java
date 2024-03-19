package com.masikga.worldcupgame.domain.etc.controller.request;

public record CreateAccessTokenRequest(
	String accessToken,
	String refreshToken
) {
}