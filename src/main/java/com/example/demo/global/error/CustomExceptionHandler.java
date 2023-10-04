package com.example.demo.global.error;

import com.example.demo.global.error.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

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

        log.warn("MethodArgumentNotValidException - {}", ex);
        return ResponseEntity
                .badRequest()
                .body(
                        new CustomErrorResponse(
                                LocalDateTime.now(),
                                CustomErrorCode.INVALID_DATA_FORMAT,
                                invalidRequestMessage,
                                "에러 고유 값"
                        )
                );
    }


    @ExceptionHandler(BaseException.class)
    ResponseEntity<CustomErrorResponse> webRequestValidException(
            BaseException ex
    ) {
        log.warn("BaseException {}", ex);
        return ResponseEntity
                .badRequest()
                .body(
                        new CustomErrorResponse(
                                LocalDateTime.now(),
                                CustomErrorCode.INVALID_DATA_FORMAT,
                                "",
                                "에러 고유 값"
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
        log.error("RuntimeException - {}", ex);
        return ResponseEntity
                .internalServerError()
                .body(
                        new CustomErrorResponse(
                                LocalDateTime.now(),
                                CustomErrorCode.SERVER_INTERNAL_ERROR,
                                "invalidRequestMessage",
                                "에러 고유 값"
                        )
                );
    }
}
