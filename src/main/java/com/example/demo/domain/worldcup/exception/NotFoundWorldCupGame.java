package com.example.demo.domain.worldcup.exception;

import com.example.demo.common.error.CustomErrorCode;
import com.example.demo.common.error.exception.BaseException;
import org.springframework.http.HttpStatus;

import static com.example.demo.common.error.CustomErrorCode.NOT_FOUND_WORLD_CUP_GAME;
import static org.springframework.http.HttpStatus.NOT_FOUND;

public class NotFoundWorldCupGame extends BaseException {
    public NotFoundWorldCupGame(String publicMessage) {
        super(publicMessage,
                NOT_FOUND_WORLD_CUP_GAME,
                NOT_FOUND);
    }
}
