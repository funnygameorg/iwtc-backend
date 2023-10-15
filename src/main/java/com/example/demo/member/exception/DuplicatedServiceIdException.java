package com.example.demo.member.exception;

import com.example.demo.common.error.CustomErrorCode;
import com.example.demo.common.error.exception.BaseException;
import org.springframework.http.HttpStatus;

import static com.example.demo.common.error.CustomErrorCode.DUPLICATED_MEMBER_SERVICE_ID;
import static org.springframework.http.HttpStatus.CONFLICT;

public class DuplicatedServiceIdException extends BaseException {
    public DuplicatedServiceIdException() {

        super("중복된 멤버 아이디", DUPLICATED_MEMBER_SERVICE_ID, CONFLICT);
    }
}
