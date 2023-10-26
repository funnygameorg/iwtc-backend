package com.example.demo.common.jpa;

import com.example.demo.common.error.CustomErrorCode;
import com.example.demo.common.error.exception.BaseException;
import org.springframework.http.HttpStatus;

public class NotFoundHoldMemberDtoException extends BaseException {
    protected NotFoundHoldMemberDtoException() {
        super(
                "Holder에 사용자의 정보가 없다",
                CustomErrorCode.NOT_FOUND_HOLD_MEMBER_DTO,
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
