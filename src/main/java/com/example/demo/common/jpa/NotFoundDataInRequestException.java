package com.example.demo.common.jpa;

import com.example.demo.common.error.CustomErrorCode;
import com.example.demo.common.error.exception.BaseException;
import org.springframework.http.HttpStatus;

public class NotFoundDataInRequestException extends BaseException {
    public NotFoundDataInRequestException() {
        super(
                "http request 에 원하는 정보가 없습니다.",
                CustomErrorCode.NOT_FOUND_DATA_IN_REQUEST,
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
