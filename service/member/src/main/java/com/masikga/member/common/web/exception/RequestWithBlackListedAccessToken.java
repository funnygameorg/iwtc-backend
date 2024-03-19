package com.masikga.member.common.web.exception;

import com.masikga.error.CustomErrorCode;
import com.masikga.error.exception.MemberBaseException;

public class RequestWithBlackListedAccessToken extends MemberBaseException {
    public RequestWithBlackListedAccessToken(String accessToken) {
        super(
                CustomErrorCode.REQUEST_WITH_BLACK_LIST_TOKEN.getMessage() + " " + accessToken,
                CustomErrorCode.REQUEST_WITH_BLACK_LIST_TOKEN,
                CustomErrorCode.REQUEST_WITH_BLACK_LIST_TOKEN.getHttpStatus()
        );

    }
}
