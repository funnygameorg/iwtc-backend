package com.example.demo.common.error;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PRIVATE;

@Getter
@NoArgsConstructor(access = PRIVATE)
public class CustomErrorResponse {

    public CustomErrorResponse(
            LocalDateTime errorTime,
            CustomErrorCode httpErrorCode,
            String message,
            String errorId
    ) {
        this.customErrorCode = httpErrorCode;
        this.errorTime = errorTime;
        this.message = message;
        this.errorId = errorId;
    }

    private String message;
    private LocalDateTime errorTime;
    private CustomErrorCode customErrorCode;
    private String errorId;


    @Override
    public String toString() {
        return "A";
    }
}
