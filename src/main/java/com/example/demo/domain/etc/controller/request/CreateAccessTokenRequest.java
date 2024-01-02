package com.example.demo.domain.etc.controller.request;

public record CreateAccessTokenRequest(
	String accessToken,
	String refreshToken
) {
}