package com.masikga.error.exception;

import com.masikga.error.CustomErrorCode;

public class NotNullArgumentExceptionMember extends WorldCupBaseException {
    public NotNullArgumentExceptionMember(Object... arguments) {
        super(
                CustomErrorCode.NOT_NULL_ARGUMENT.getMessage() + arguments,
                CustomErrorCode.NOT_NULL_ARGUMENT,
                CustomErrorCode.NOT_NULL_ARGUMENT.getHttpStatus()
        );
    }
}
