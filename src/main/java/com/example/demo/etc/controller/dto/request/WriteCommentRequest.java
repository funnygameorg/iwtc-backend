package com.example.demo.etc.controller.dto.request;

import com.example.demo.etc.model.vo.ContentsType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema
public record WriteCommentRequest(
        @Schema(description = "의견 내용")
        @NotBlank(message = "의견 내용 : 필수 값")
        String commentContents,

        @Schema(description = "작성자 별칭")
        @NotBlank(message = "작성자 별칭 : 필수 값")
        String nickname,

        @Schema(description = "컨텐츠 타입")
        @NotBlank(message = "컨텐츠 타입 : 필수 값")
        ContentsType contentsType
) {
    // 생성자, 필드 액세스 메서드 등이 자동으로 생성됩니다.
}
