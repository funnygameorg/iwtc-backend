package com.masikga.member.common.jpa;

import com.masikga.error.exception.MemberBaseException;

import static com.masikga.error.CustomErrorCode.NOT_FOUND_DATA_IN_REQUEST;

public class NotFoundDataInRequestExceptionMember extends MemberBaseException {
    public NotFoundDataInRequestExceptionMember() {
        super(
                NOT_FOUND_DATA_IN_REQUEST.getMessage(),
                NOT_FOUND_DATA_IN_REQUEST,
                NOT_FOUND_DATA_IN_REQUEST.getHttpStatus()
        );
    }
}
