package com.masikga.member.controller;

import com.masikga.error.CustomErrorCode;
import com.masikga.error.CustomErrorResponse;
import com.masikga.error.exception.MemberBaseException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Arrays;

import static java.util.UUID.randomUUID;
import static org.springframework.core.Ordered.LOWEST_PRECEDENCE;

@Slf4j
@Order(LOWEST_PRECEDENCE)
@RestControllerAdvice
public class CustomMemberExceptionHandler {

    /*
        사용자가 요청안에 양식에 부합하지 않은 데이터를 포함
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<CustomErrorResponse> invalidRequestException(MethodArgumentNotValidException ex) {

        String invalidRequestMessage = getBindingErrorMessage(ex);

        var body = generateLog("handled", invalidRequestMessage);
        log.warn(body);

        return ResponseEntity
                .badRequest()
                .body(CustomErrorResponse.builder()
                        .errorCode(CustomErrorCode.INVALID_CLIENT_REQUEST_BODY.getCode())
                        .message(invalidRequestMessage)
                        .errorTime(LocalDateTime.now())
                        .errorId(randomUUID().toString())
                        .build()
                );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    ResponseEntity<CustomErrorResponse> invalidRequestException2(ConstraintViolationException ex) {

        String invalidRequestMessage = getConstraintViolationMessage(ex);

        var body = generateLog("handled", invalidRequestMessage);
        log.warn(body);

        return ResponseEntity
                .badRequest()
                .body(CustomErrorResponse.builder()
                        .errorCode(CustomErrorCode.INVALID_CLIENT_REQUEST_BODY.getCode())
                        .errorTime(LocalDateTime.now())
                        .message(invalidRequestMessage)
                        .errorId(randomUUID().toString())
                        .build()
                );
    }

    @ExceptionHandler(MemberBaseException.class)
    ResponseEntity<CustomErrorResponse> handledException(MemberBaseException ex) {

        var body = generateLog("handled", ex.toString());
        log.warn(body);

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

    // TODO : BaseException을 상속하지만 위험한 에러

    /*
        핸들링하지 못한 예외
     */
    @ExceptionHandler(RuntimeException.class)
    ResponseEntity<CustomErrorResponse> notHandledException(RuntimeException ex) {

        var body = generateLog("not_handle", ex.toString() + " / " + Arrays.toString(ex.getStackTrace()));
        log.error(body);

        return ResponseEntity
                .internalServerError()
                .body(CustomErrorResponse.builder()
                        .errorTime(LocalDateTime.now())
                        .errorCode(CustomErrorCode.SERVER_INTERNAL_ERROR.getCode())
                        .message(CustomErrorCode.SERVER_INTERNAL_ERROR.getMessage())
                        .errorId(randomUUID().toString())
                        .build()
                );
    }

    private String generateLog(String level, String msg) {
        return "{'lv' : '%s', 'msg': '%s'}".formatted(level, msg);
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
