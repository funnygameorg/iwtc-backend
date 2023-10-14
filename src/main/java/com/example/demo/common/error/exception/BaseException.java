package com.example.demo.common.error.exception;

import lombok.*;
import org.springframework.boot.logging.LogLevel;

import java.time.LocalDateTime;
import java.util.UUID;

import static lombok.AccessLevel.PROTECTED;



@Getter
@NoArgsConstructor(access = PROTECTED)
public abstract class BaseException extends RuntimeException {
    protected BaseException(
            String memberId,
            LogLevel logLevel,
            LocalDateTime errorTime
    ) {
        this.logLevel = logLevel;
        this.errorTime  = errorTime;
        this.errorId = errorTime + "__" + memberId + "__" + UUID.randomUUID();
    }
    // 에러 발생 시각
    protected LocalDateTime errorTime;
    // 로그 수준
    protected LogLevel logLevel;
    // 에러 고유 값
    protected String errorId;
}
