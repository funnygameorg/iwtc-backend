package com.masikga.worldcupgame.domain.worldcup.exception;

import com.masikga.error.CustomErrorCode;
import com.masikga.error.exception.WorldCupBaseException;

import java.util.Objects;

public class NotFoundWorldCupGameExceptionMember extends WorldCupBaseException {

    public NotFoundWorldCupGameExceptionMember(String publicMessage) {
        super(
                CustomErrorCode.NOT_FOUND_WORLD_CUP_GAME.getMessage() + " " + publicMessage,
                CustomErrorCode.DUPLICATED_WORLD_CUP_GAME_TITLE.NOT_FOUND_WORLD_CUP_GAME,
                CustomErrorCode.NOT_FOUND_WORLD_CUP_GAME.getHttpStatus());
    }

    public NotFoundWorldCupGameExceptionMember(Long worldCupId) {
        this(Objects.toString(worldCupId));
    }
}
