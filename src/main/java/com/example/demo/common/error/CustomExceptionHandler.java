package com.example.demo.common.error;

import com.example.demo.common.error.exception.BaseException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
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
import static java.util.UUID.randomUUID;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

    /*
        사용자가 요청안에 양식에 부합하지 않은 데이터를 포함
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<CustomErrorResponse> invalidRequestException(MethodArgumentNotValidException ex) {
        String invalidRequestMessage = getBindingErrorMessage(ex);

        log.info("invalidRequestException - {}", invalidRequestMessage);

        return ResponseEntity
                .badRequest()
                .body(CustomErrorResponse.builder()
                        .errorCode(INVALID_CLIENT_REQUEST_BODY.getCode())
                        .message(INVALID_CLIENT_REQUEST_BODY.getMessage() + " " + invalidRequestMessage)
                        .errorTime(LocalDateTime.now())
                        .errorId(randomUUID().toString())
                        .build()
                );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    ResponseEntity<CustomErrorResponse> invalidRequestException2(ConstraintViolationException ex) {
        String invalidRequestMessage = getConstraintViolationMessage(ex);

        log.info("invalidRequestException - {}", invalidRequestMessage);

        return ResponseEntity
                .badRequest()
                .body(CustomErrorResponse.builder()
                        .errorCode(INVALID_CLIENT_REQUEST_BODY.getCode())
                        .errorTime(LocalDateTime.now())
                        .message(invalidRequestMessage)
                        .errorId(randomUUID().toString())
                        .build()
                );
    }

    @ExceptionHandler(BaseException.class)
    ResponseEntity<CustomErrorResponse> handledException(BaseException ex) {

        log.info("BaseException - {}", ex);

        return ResponseEntity
                .status(ex.getHttpStatus())
                .body(CustomErrorResponse.builder()
                        .errorTime(ex.getErrorTime())
                        .errorCode(ex.getErrorCode().getCode())
                        .message(ex.getPublicMessage())
                        .errorId(ex.getErrorId())
                        .build()
                );
    }
    /*
        핸들링하지 못한 예외
     */
    @ExceptionHandler(RuntimeException.class)
    ResponseEntity<CustomErrorResponse> notHandledException(RuntimeException ex) {

        log.warn("RuntimeException - {}", ex);

        return ResponseEntity
                .internalServerError()
                .body(CustomErrorResponse.builder()
                        .errorTime(LocalDateTime.now())
                        .errorCode(SERVER_INTERNAL_ERROR.getCode())
                        .message(SERVER_INTERNAL_ERROR.getMessage())
                        .errorId(randomUUID().toString())
                        .build()
                );
    }


    private String getBindingErrorMessage(MethodArgumentNotValidException ex) {
        return ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList()
                .toString();
    }

    private String getConstraintViolationMessage(ConstraintViolationException ex) {
        return ex.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .toList()
                .toString();
    }
}
