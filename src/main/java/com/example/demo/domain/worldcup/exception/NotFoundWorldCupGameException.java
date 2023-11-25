package com.example.demo.domain.worldcup.exception;

import com.example.demo.common.error.exception.BaseException;

import java.util.Objects;

import static com.example.demo.common.error.CustomErrorCode.NOT_FOUND_WORLD_CUP_GAME;
import static org.springframework.http.HttpStatus.NOT_FOUND;

public class NotFoundWorldCupGameException extends BaseException {

    public NotFoundWorldCupGameException(String publicMessage) {
        super(
                NOT_FOUND_WORLD_CUP_GAME.getMessage() + " " + publicMessage,
                NOT_FOUND_WORLD_CUP_GAME,
                NOT_FOUND_WORLD_CUP_GAME.getHttpStatus());
    }

    public NotFoundWorldCupGameException(Long worldCupId) {
        this(Objects.toString(worldCupId));
    }
}
