package com.masikga.member.common.web.exception;


import com.masikga.error.CustomErrorCode;
import com.masikga.error.exception.MemberBaseException;

public class ExpiredAuthenticationExceptionMember extends MemberBaseException {
    public ExpiredAuthenticationExceptionMember() {
        super(
                CustomErrorCode.EXPIRED_REMEMBER_ME.getMessage(),
                CustomErrorCode.EXPIRED_REMEMBER_ME,
                CustomErrorCode.EXPIRED_REMEMBER_ME.getHttpStatus()
        );
    }
}
