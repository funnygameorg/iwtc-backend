package com.masikga.itwc.common.web;

public record RestApiResponse<T>(
	int code,
	String message,
	T data
) {
}