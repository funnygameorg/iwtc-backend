package com.masikga.worldcupgame.domain.etc.exception;

import com.masikga.error.exception.WorldCupBaseException;

public class NotSupportedFileExtensionExceptionMember extends WorldCupBaseException {

    public NotSupportedFileExtensionExceptionMember(String requestType) {
        super(requestType, null, null);
    }
}
