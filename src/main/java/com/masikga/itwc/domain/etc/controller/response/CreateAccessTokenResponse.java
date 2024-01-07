package com.masikga.itwc.domain.etc.controller.response;

public record CreateAccessTokenResponse(
	String newAccessToken,
	String refreshToken
) {

}
