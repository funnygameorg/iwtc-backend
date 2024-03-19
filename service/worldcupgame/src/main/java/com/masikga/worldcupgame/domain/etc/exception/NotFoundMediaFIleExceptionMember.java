package com.masikga.worldcupgame.domain.etc.exception;

import com.masikga.error.CustomErrorCode;
import com.masikga.error.exception.WorldCupBaseException;

public class NotFoundMediaFIleExceptionMember extends WorldCupBaseException {

    public NotFoundMediaFIleExceptionMember(long mediaFileId) {
        super(
                CustomErrorCode.NOT_FOUND_MEDIA_FILE.getMessage() + mediaFileId,
                CustomErrorCode.NOT_FOUND_MEDIA_FILE,
                CustomErrorCode.NOT_FOUND_MEDIA_FILE.getHttpStatus()
        );
    }

}
