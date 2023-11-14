package com.example.demo.domain.member.exception;

import com.example.demo.common.error.exception.BaseException;

import static com.example.demo.common.error.CustomErrorCode.NOT_FOUND_MEMBER;
import static org.springframework.http.HttpStatus.NOT_FOUND;

public class NotFoundMemberException extends BaseException {

    public NotFoundMemberException() {
        super(
                NOT_FOUND_MEMBER.getMessage(),
                NOT_FOUND_MEMBER,
                NOT_FOUND_MEMBER.getHttpStatus()
        );
    }
}
