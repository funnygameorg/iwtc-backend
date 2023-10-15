package com.example.demo.common.error;

import com.example.demo.common.error.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.example.demo.common.error.CustomErrorCode.INVALID_CLIENT_REQUEST_BODY;
import static com.example.demo.common.error.CustomErrorCode.SERVER_INTERNAL_ERROR;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

    /*
        사용자가 요청안에 양식에 부합하지 않은 데이터를 포함
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<CustomErrorResponse> webRequestValidException(
            MethodArgumentNotValidException ex
    ) {
        String invalidRequestMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList()
                .toString();

        log.info("MethodArgumentNotValidException - {}", invalidRequestMessage);
        return ResponseEntity
                .badRequest()
                .body(
                        new CustomErrorResponse(
                                LocalDateTime.now(),
                                INVALID_CLIENT_REQUEST_BODY,
                                invalidRequestMessage,
                                UUID.randomUUID().toString()
                        )
                );
    }


    @ExceptionHandler(BaseException.class)
    ResponseEntity<CustomErrorResponse> webRequestValidException(
            BaseException ex
    ) {
        log.info("BaseException {}", ex);
        return ResponseEntity
                .badRequest()
                .body(
                        new CustomErrorResponse(
                                ex.getErrorTime(),
                                ex.getErrorCode(),
                                ex.getMessage(),
                                ex.getErrorId()
                        )
                );
    }
    /*
        핸들링하지 못한 예외
     */
    @ExceptionHandler(RuntimeException.class)
    ResponseEntity<CustomErrorResponse> webRequestValidException(
            RuntimeException ex
    ) {
        log.warn("RuntimeException - {}", ex);
        return ResponseEntity
                .internalServerError()
                .body(
                        new CustomErrorResponse(
                                LocalDateTime.now(),
                                SERVER_INTERNAL_ERROR,
                                "관리자에게 연락해주세요.",
                                UUID.randomUUID().toString()
                        )
                );
    }
}
