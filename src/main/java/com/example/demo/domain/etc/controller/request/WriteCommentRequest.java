package com.example.demo.domain.etc.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Schema
@Builder
public record WriteCommentRequest(

	@Schema(description = "의견 내용")
	@NotBlank(message = "의견 내용 : 필수 값")
	@Size(min = 1, max = 30, message = "댓글 내용 1 ~ 30자")
	String body,

	@Schema(description = "사용자 닉네임")
	String nickname
) {
}
