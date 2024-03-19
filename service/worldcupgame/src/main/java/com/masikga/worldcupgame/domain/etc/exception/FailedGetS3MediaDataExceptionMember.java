package com.masikga.worldcupgame.domain.etc.exception;

import com.masikga.error.CustomErrorCode;
import com.masikga.error.exception.WorldCupBaseException;

public class FailedGetS3MediaDataExceptionMember extends WorldCupBaseException {

    public FailedGetS3MediaDataExceptionMember(
            String objectKey,
            CustomErrorCode errorCode
    ) {
        super(
                errorCode.getMessage() + objectKey,
                errorCode,
                errorCode.getHttpStatus()
        );
    }

}
