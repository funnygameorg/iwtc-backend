package com.masikga.member.exception;

import com.masikga.error.CustomErrorCode;
import com.masikga.error.exception.MemberBaseException;

public class NotFoundMemberExceptionMember extends MemberBaseException {

    public NotFoundMemberExceptionMember() {

        super(
                CustomErrorCode.NOT_FOUND_MEMBER.getMessage(),
                CustomErrorCode.NOT_FOUND_MEMBER,
                CustomErrorCode.NOT_FOUND_MEMBER.getHttpStatus()
        );
    }
}
