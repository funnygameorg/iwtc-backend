package com.masikga.worldcupgame.common.web.exception;

import com.masikga.error.CustomErrorCode;
import com.masikga.error.exception.WorldCupBaseException;

public class RequestWithBlackListedAccessToken extends WorldCupBaseException {
    public RequestWithBlackListedAccessToken(String accessToken) {
        super(
                CustomErrorCode.REQUEST_WITH_BLACK_LIST_TOKEN.getMessage() + " " + accessToken,
                CustomErrorCode.REQUEST_WITH_BLACK_LIST_TOKEN,
                CustomErrorCode.REQUEST_WITH_BLACK_LIST_TOKEN.getHttpStatus()
        );

    }
}
