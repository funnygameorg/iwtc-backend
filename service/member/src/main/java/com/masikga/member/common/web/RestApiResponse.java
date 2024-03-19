package com.masikga.member.common.web;

public record RestApiResponse<T>(
	int code,
	String message,
	T data
) {
}