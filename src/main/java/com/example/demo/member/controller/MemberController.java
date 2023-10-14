package com.example.demo.member.controller;

import com.example.demo.member.controller.dto.SignUpRequest;
import com.example.demo.common.error.CustomErrorResponse;
import com.example.demo.common.web.RestApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Member",
        description = "계정 관련 API"
)
@RestController
@RequestMapping("/members")
public class MemberController {

    @Operation(
            summary = "일반 회원가입",
            description = "아무런 조건없이 가입이 가능합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "가입 성공",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "validation request",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomErrorResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "[아이디 중복, 이메일 중복, 닉네임 중복]",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomErrorResponse.class))
                    )
            }
    )
    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public RestApiResponse<Object> signUp(
            @Valid @RequestBody SignUpRequest request
    ) {
        return new RestApiResponse(
                1,
                "가입 성공",
                null
        );
    }
}
