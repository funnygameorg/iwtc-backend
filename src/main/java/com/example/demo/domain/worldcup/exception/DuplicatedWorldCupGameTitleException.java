package com.example.demo.domain.worldcup.exception;

import com.example.demo.common.error.exception.BaseException;

import static com.example.demo.common.error.CustomErrorCode.DUPLICATED_WORLD_CUP_GAME_TITLE;

public class DuplicatedWorldCupGameTitleException extends BaseException {

    public DuplicatedWorldCupGameTitleException(String titleName) {

        super(
                DUPLICATED_WORLD_CUP_GAME_TITLE.getMessage() + " " +titleName,
                DUPLICATED_WORLD_CUP_GAME_TITLE,
                DUPLICATED_WORLD_CUP_GAME_TITLE.getHttpStatus()
        );
    }
}
