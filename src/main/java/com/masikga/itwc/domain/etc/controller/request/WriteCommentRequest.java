package com.masikga.itwc.domain.etc.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Schema
@Builder
public record WriteCommentRequest(

	@Schema(description = "의견 내용")
	@NotNull(message = "데이터가 존재하지 않습니다.(not null)")
	@Size(min = 1, max = 30, message = "댓글 내용 1 ~ 30자")
	String body,

	@Schema(description = "사용자 닉네임")
	String nickname
) {
}
