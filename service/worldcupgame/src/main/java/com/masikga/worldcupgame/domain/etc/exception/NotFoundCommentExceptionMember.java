package com.masikga.worldcupgame.domain.etc.exception;

import com.masikga.error.CustomErrorCode;
import com.masikga.error.exception.WorldCupBaseException;

public class NotFoundCommentExceptionMember extends WorldCupBaseException {
    public NotFoundCommentExceptionMember(Long commentId) {
        super(
                CustomErrorCode.NOT_FOUND_COMMENT.getMessage() + commentId,
                CustomErrorCode.NOT_FOUND_COMMENT,
                CustomErrorCode.NOT_FOUND_COMMENT.getHttpStatus()
        );
    }
}
