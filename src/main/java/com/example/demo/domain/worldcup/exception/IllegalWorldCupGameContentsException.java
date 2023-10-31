package com.example.demo.domain.worldcup.exception;

import com.example.demo.common.error.exception.BaseException;

import static com.example.demo.common.error.CustomErrorCode.ILLEGAL_WORLD_CUP_GAME_CONTENTS;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class IllegalWorldCupGameContentsException extends BaseException {
    public IllegalWorldCupGameContentsException(String publicMessage) {
        super(
                publicMessage,
                ILLEGAL_WORLD_CUP_GAME_CONTENTS,
                BAD_REQUEST
        );
    }
}