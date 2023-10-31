package com.example.demo.domain.worldcup.exception;

import com.example.demo.common.error.CustomErrorCode;
import com.example.demo.common.error.exception.BaseException;
import org.springframework.http.HttpStatus;

import static com.example.demo.common.error.CustomErrorCode.NOT_SUPPORTED_GAME_ROUND;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class NotSupportedGameRoundException extends BaseException {
    public NotSupportedGameRoundException(String publicMessage) {
        super(publicMessage, NOT_SUPPORTED_GAME_ROUND, BAD_REQUEST);
    }
}
