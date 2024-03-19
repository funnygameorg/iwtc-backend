package com.masikga.feign

import com.masikga.error.CustomErrorResponse
import org.springframework.core.Ordered.HIGHEST_PRECEDENCE
import org.springframework.core.annotation.Order
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.LocalDateTime
import java.util.*

@Order(HIGHEST_PRECEDENCE)
@RestControllerAdvice
class FeignExceptionHandler {

    @ExceptionHandler(FeignException::class)
    fun feignException(ex: FeignException): ResponseEntity<CustomErrorResponse> {
        return ResponseEntity
            .internalServerError()
            .body(
                CustomErrorResponse.builder()
                    .errorTime(LocalDateTime.now())
                    .errorCode(ex.code)
                    .message(ex.customMessage)
                    .errorId(UUID.randomUUID().toString())
                    .build()
            )
    }

}