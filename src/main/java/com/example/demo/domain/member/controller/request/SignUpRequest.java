package com.example.demo.domain.member.controller.request;

import com.example.demo.common.web.validation.NoSpace;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Schema
@Builder
public record SignUpRequest (
        @Schema(description = "서비스에서 사용하는 아이디")
        @Size(min=6, max = 20, message = "사용자 아이디 : 6자리 이상, 20자리 이하")
        @NoSpace
        String serviceId,

        @Schema(description = "사용자 닉네임")
        @Size(min = 2, max = 10, message = "사용자 닉네임 : 2자리 이상, 10자리 이하")
        @NoSpace
        String nickname,

        @Schema(description = "암호")
        @Size(min = 6, message = "사용자 암호 : 6자리 이상")
        @NoSpace
        String password
) {

}