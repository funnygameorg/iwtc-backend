package com.masikga.worldcupgame.common.web;

public record RestApiResponse<T>(
	int code,
	String message,
	T data
) {
}