package com.example.demo.common.error.exception;

import com.example.demo.common.error.CustomErrorCode;
import org.springframework.http.HttpStatus;

import static com.example.demo.common.error.CustomErrorCode.NOT_NULL_ARGUMENT;

public class NotNullArgumentException extends BaseException {
    public NotNullArgumentException(Object... arguments) {
        super(
                NOT_NULL_ARGUMENT.getMessage() + arguments,
                NOT_NULL_ARGUMENT,
                NOT_NULL_ARGUMENT.getHttpStatus()
        );
    }
}
