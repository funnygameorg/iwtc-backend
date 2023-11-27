package com.example.demo.domain.worldcup.controller.validator.worldcup;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "월드컵 이름")
@NotBlank(message = "월드컵 제목: 필수 값")
public @interface VerifyWorldCupTitle {
}
