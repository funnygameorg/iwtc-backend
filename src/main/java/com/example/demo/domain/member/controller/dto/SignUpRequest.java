package com.example.demo.domain.member.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema
public record SignUpRequest (
        @Schema(description = "서비스에서 사용하는 아이디")
        @Size(max = 10, message = "사용자 아이디 : 10자리 이하")
        @NotBlank(message = "사용자 아이디 : 필수 값")
        String serviceId,

        @Schema(description = "사용자 이메일")
        @NotBlank(message = "이메일 : 필수 값")
        @Email
        String email,

        @Schema(description = "사용자 닉네임")
        @NotBlank(message = "별칭: 필수 값")
        String nickname,

        @Schema(description = "암호")
        @NotBlank(message = "암호: 필수 값")
        String password
) {

}