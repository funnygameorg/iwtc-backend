package com.example.demo.domain.member.exception;

import com.example.demo.common.error.exception.BaseException;

import static com.example.demo.common.error.CustomErrorCode.DUPLICATED_MEMBER_NICKNAME;
import static org.springframework.http.HttpStatus.CONFLICT;

public class DuplicatedNicknameException extends BaseException {
    public DuplicatedNicknameException() {
        super(
                DUPLICATED_MEMBER_NICKNAME.getMessage(),
                DUPLICATED_MEMBER_NICKNAME,
                DUPLICATED_MEMBER_NICKNAME.getHttpStatus()
        );
    }
}
