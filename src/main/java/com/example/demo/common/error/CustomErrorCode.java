package com.example.demo.common.error;

import com.example.demo.common.error.entity.ErrorCodeRepository;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum CustomErrorCode {
    DUPLICATED_MEMBER_SERVICE_ID,
    DUPLICATED_MEMBER_NICKNAME,
    DUPLICATED_WORLD_CUP_GAME_TITLE,
    SERVER_INTERNAL_ERROR,
    NOT_FOUND_MEMBER,
    NOT_FOUND_MEDIA_FILE,
    INVALID_CLIENT_REQUEST_BODY,
    INVALID_TOKEN_EXCEPTION,
    REQUEST_WITH_BLACK_LIST_TOKEN,
    NOT_FOUND_DATA_IN_REQUEST,
    NOT_FOUND_WORLD_CUP_GAME,
    NO_ROUNDS_AVAILABLE_TO_PLAY,
    NOT_SUPPORTED_GAME_ROUND,
    ILLEGAL_WORLD_CUP_GAME_CONTENTS,
    NOT_OWNER_GAME,
    NOT_FOUND_WORLD_CUP_GAME_CONTENTS,
    EXPIRED_REMEMBER_ME,

    NOT_EXISTS_S3_MEDIA_FILE;

    private int code;
    private String message;
    private HttpStatus httpStatus;

    @Component
    @RequiredArgsConstructor
    public static class ErrorInjector {
        private final ErrorCodeRepository repository;

        @PostConstruct
        public void postConstruct() {
            Arrays.stream(CustomErrorCode.values())
                    .forEach(errorCode ->
                            repository.findByName(errorCode.name())
                                    .ifPresent( e -> {
                                        errorCode.code = e.getCode();
                                        errorCode.httpStatus = HttpStatus.valueOf(e.getHttpStatus());
                                        errorCode.message = e.getMessage();
                                    }
                            )
                    );
        }
    }

}