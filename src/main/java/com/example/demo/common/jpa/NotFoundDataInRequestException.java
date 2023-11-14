package com.example.demo.common.jpa;

import com.example.demo.common.error.CustomErrorCode;
import com.example.demo.common.error.exception.BaseException;
import org.springframework.http.HttpStatus;

import static com.example.demo.common.error.CustomErrorCode.*;

public class NotFoundDataInRequestException extends BaseException {
    public NotFoundDataInRequestException() {
        super(
                NOT_FOUND_DATA_IN_REQUEST.getMessage(),
                NOT_FOUND_DATA_IN_REQUEST,
                NOT_FOUND_DATA_IN_REQUEST.getHttpStatus()
        );
    }
}
