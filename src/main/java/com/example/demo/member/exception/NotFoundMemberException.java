package com.example.demo.member.exception;

import com.example.demo.common.error.exception.BaseException;

import static com.example.demo.common.error.CustomErrorCode.NOT_FOUND_MEMBER;
import static org.springframework.http.HttpStatus.NOT_FOUND;

public class NotFoundMemberException extends BaseException {

    public NotFoundMemberException() {
        super(
                "존재하지 않는 사용자입니다.",
                NOT_FOUND_MEMBER,
                NOT_FOUND
        );
    }
}
