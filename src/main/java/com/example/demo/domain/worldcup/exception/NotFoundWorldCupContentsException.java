package com.example.demo.domain.worldcup.exception;

import com.example.demo.common.error.CustomErrorCode;
import com.example.demo.common.error.exception.BaseException;
import org.springframework.http.HttpStatus;

import static com.example.demo.common.error.CustomErrorCode.*;

public class NotFoundWorldCupContentsException extends BaseException {
    public NotFoundWorldCupContentsException(String publicMessage) {
        super(
                publicMessage,
                NOT_FOUND_WORLD_CUP_GAME_CONTENTS,
                HttpStatus.NOT_FOUND
        );
    }
}
