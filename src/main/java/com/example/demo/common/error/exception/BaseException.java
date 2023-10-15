package com.example.demo.common.error.exception;

import com.example.demo.common.error.CustomErrorCode;
import lombok.*;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;



@Getter
@NoArgsConstructor(access = PRIVATE)
public abstract class BaseException extends RuntimeException {

    protected BaseException(String message, CustomErrorCode errorCode, HttpStatus httpStatus) {

        this.message = message;
        this.errorCode = errorCode;
        this.errorTime  = LocalDateTime.now();
        this.httpStatus = httpStatus;
        this.errorId = new StringBuilder()
                .append(errorTime)
                .append("__")
                .append(UUID.randomUUID())
                .toString();
    }

    LocalDateTime errorTime;
    String errorId;
    String message;
    CustomErrorCode errorCode;
    HttpStatus httpStatus;
}
