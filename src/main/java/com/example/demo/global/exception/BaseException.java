package com.example.demo.global.exception;

import lombok.AllArgsConstructor;
import org.springframework.boot.logging.LogLevel;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
public abstract class BaseException extends RuntimeException {
    protected ErrorCode errorCode; // 에러 카테고리 고유 값

    protected LocalDateTime errorTime = LocalDateTime.now(); // 에러 발생 시각

    private String memberId; // 에러와 관련된 사용자 고유 값

    protected LogLevel logLevel; // 로그 수준

    protected String msg; // 에러 내용

    RuntimeException causeException; // 에러 원인
    // 에러 고유 값
    protected String errorId = errorTime + memberId + UUID.randomUUID();
}
