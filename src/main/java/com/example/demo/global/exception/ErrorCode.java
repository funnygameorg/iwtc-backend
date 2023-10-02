package com.example.demo.global.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ErrorCode {
    private ErrorCategory errorCategory;
    private String errorDescription;
}