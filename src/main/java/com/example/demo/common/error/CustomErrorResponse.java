package com.example.demo.common.error;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PRIVATE;

@Getter
@ToString
@NoArgsConstructor(access = PRIVATE)
public class CustomErrorResponse {

    public CustomErrorResponse(
            LocalDateTime errorTime,
            CustomErrorCode errorCode,
            String message,
            String errorId
    ) {
        this.customErrorCode = errorCode;
        this.errorTime = errorTime;
        this.message = message;
        this.errorId = errorId;
    }

    private String message;
    private LocalDateTime errorTime;
    private CustomErrorCode customErrorCode;
    private String errorId;
}
