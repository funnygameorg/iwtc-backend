package com.example.demo.member.exception;

import com.example.demo.common.error.CustomErrorCode;
import com.example.demo.common.error.exception.BaseException;
import org.springframework.http.HttpStatus;

import static com.example.demo.common.error.CustomErrorCode.DUPLICATED_MEMBER_NICKNAME;
import static org.springframework.http.HttpStatus.CONFLICT;

public class DuplicatedNicknameException extends BaseException {
    public DuplicatedNicknameException() {
        super("중복된 닉네임", DUPLICATED_MEMBER_NICKNAME, CONFLICT);
    }
}
