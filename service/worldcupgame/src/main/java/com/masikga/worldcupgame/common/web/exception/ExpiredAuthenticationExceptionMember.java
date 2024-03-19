package com.masikga.worldcupgame.common.web.exception;

import com.masikga.error.CustomErrorCode;
import com.masikga.error.exception.WorldCupBaseException;

public class ExpiredAuthenticationExceptionMember extends WorldCupBaseException {
    public ExpiredAuthenticationExceptionMember() {
        super(
                CustomErrorCode.EXPIRED_REMEMBER_ME.getMessage(),
                CustomErrorCode.EXPIRED_REMEMBER_ME,
                CustomErrorCode.EXPIRED_REMEMBER_ME.getHttpStatus()
        );
    }
}