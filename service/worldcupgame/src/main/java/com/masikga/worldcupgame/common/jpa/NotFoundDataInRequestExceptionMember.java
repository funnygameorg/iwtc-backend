package com.masikga.worldcupgame.common.jpa;

import com.masikga.error.exception.WorldCupBaseException;

import static com.masikga.error.CustomErrorCode.NOT_FOUND_DATA_IN_REQUEST;

public class NotFoundDataInRequestExceptionMember extends WorldCupBaseException {
    public NotFoundDataInRequestExceptionMember() {
        super(
                NOT_FOUND_DATA_IN_REQUEST.getMessage(),
                NOT_FOUND_DATA_IN_REQUEST,
                NOT_FOUND_DATA_IN_REQUEST.getHttpStatus()
        );
    }
}
