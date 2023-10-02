package com.example.demo.global.web;

import lombok.Builder;

@Builder
public record RestApiResponse<T> (
    int code,
    String message,
    T data
) {
}