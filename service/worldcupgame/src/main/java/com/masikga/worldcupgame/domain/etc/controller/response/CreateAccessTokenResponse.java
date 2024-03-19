package com.masikga.worldcupgame.domain.etc.controller.response;

public record CreateAccessTokenResponse(
	String newAccessToken,
	String refreshToken
) {

}
