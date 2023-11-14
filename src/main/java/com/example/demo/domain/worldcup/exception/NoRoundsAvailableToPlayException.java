package com.example.demo.domain.worldcup.exception;

import com.example.demo.common.error.CustomErrorCode;
import com.example.demo.common.error.exception.BaseException;
import org.springframework.http.HttpStatus;

import static com.example.demo.common.error.CustomErrorCode.NO_ROUNDS_AVAILABLE_TO_PLAY;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class NoRoundsAvailableToPlayException extends BaseException {
    public NoRoundsAvailableToPlayException(String publicMessage) {
        super(
                NO_ROUNDS_AVAILABLE_TO_PLAY.getMessage() + " " +publicMessage,
                NO_ROUNDS_AVAILABLE_TO_PLAY,
                NO_ROUNDS_AVAILABLE_TO_PLAY.getHttpStatus()
        );
    }
}
