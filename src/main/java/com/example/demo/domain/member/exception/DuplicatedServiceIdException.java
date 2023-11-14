package com.example.demo.domain.member.exception;

import com.example.demo.common.error.exception.BaseException;

import static com.example.demo.common.error.CustomErrorCode.DUPLICATED_MEMBER_SERVICE_ID;
import static org.springframework.http.HttpStatus.CONFLICT;

public class DuplicatedServiceIdException extends BaseException {
    public DuplicatedServiceIdException() {

        super(
                DUPLICATED_MEMBER_SERVICE_ID.getMessage(),
                DUPLICATED_MEMBER_SERVICE_ID,
                DUPLICATED_MEMBER_SERVICE_ID.getHttpStatus()
        );
    }
}
