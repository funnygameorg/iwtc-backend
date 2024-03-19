package com.masikga.worldcupgame.domain.worldcup.exception;

import com.masikga.error.CustomErrorCode;
import com.masikga.error.exception.WorldCupBaseException;

public class DuplicatedWorldCupGameTitleExceptionMember extends WorldCupBaseException {

    public DuplicatedWorldCupGameTitleExceptionMember(String titleName) {

        super(
                CustomErrorCode.DUPLICATED_WORLD_CUP_GAME_TITLE.getMessage() + " " + titleName,
                CustomErrorCode.DUPLICATED_WORLD_CUP_GAME_TITLE.DUPLICATED_WORLD_CUP_GAME_TITLE,
                CustomErrorCode.DUPLICATED_WORLD_CUP_GAME_TITLE.getHttpStatus()
        );
    }
}
