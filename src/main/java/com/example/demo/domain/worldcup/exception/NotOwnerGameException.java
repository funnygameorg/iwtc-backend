package com.example.demo.domain.worldcup.exception;

import com.example.demo.common.error.CustomErrorCode;
import com.example.demo.common.error.exception.BaseException;
import org.springframework.http.HttpStatus;

import static com.example.demo.common.error.CustomErrorCode.NOT_OWNER_GAME;

public class NotOwnerGameException extends BaseException {
    public NotOwnerGameException() {
        super(
                "사용자가 작성한 게임이 아닙니다.",
                NOT_OWNER_GAME,
                HttpStatus.BAD_REQUEST
        );
    }
}
