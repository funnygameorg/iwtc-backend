package com.masikga.member.exception;

import com.masikga.error.CustomErrorCode;
import com.masikga.error.exception.MemberBaseException;

public class DuplicatedNicknameExceptionMember extends MemberBaseException {
    public DuplicatedNicknameExceptionMember() {
        super(
                CustomErrorCode.DUPLICATED_MEMBER_NICKNAME.getMessage(),
                CustomErrorCode.DUPLICATED_MEMBER_NICKNAME,
                CustomErrorCode.DUPLICATED_MEMBER_NICKNAME.getHttpStatus()
        );
    }
}
