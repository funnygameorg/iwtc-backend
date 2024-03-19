package com.masikga.member.exception;

import com.masikga.error.CustomErrorCode;
import com.masikga.error.exception.MemberBaseException;

public class DuplicatedServiceIdExceptionMember extends MemberBaseException {
    public DuplicatedServiceIdExceptionMember() {

        super(
                CustomErrorCode.DUPLICATED_MEMBER_SERVICE_ID.getMessage(),
                CustomErrorCode.DUPLICATED_MEMBER_SERVICE_ID,
                CustomErrorCode.DUPLICATED_MEMBER_SERVICE_ID.getHttpStatus()
        );
    }
}
