package com.masikga.worldcupgame.domain.etc.exception;

import com.masikga.error.CustomErrorCode;
import com.masikga.error.exception.WorldCupBaseException;

public class NotOwnerCommentExceptionMember extends WorldCupBaseException {

    public NotOwnerCommentExceptionMember(Long memberId) {

        super(
                CustomErrorCode.NOT_OWNER_COMMENT.getMessage() + memberId,
                CustomErrorCode.NOT_OWNER_COMMENT,
                CustomErrorCode.NOT_OWNER_COMMENT.getHttpStatus()
        );

    }
}
