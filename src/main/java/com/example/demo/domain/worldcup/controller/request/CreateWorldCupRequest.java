package com.example.demo.domain.worldcup.controller.request;

import com.example.demo.domain.worldcup.model.entity.vo.VisibleType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

@Schema
public record CreateWorldCupRequest (
    @Schema(description = "월드컵 식별자")
    Long worldCupId,

    @Schema(description = "월드컵 이름")
    @NotBlank(message = "월드컵 제목: 필수 값")
    String title,

    @Schema(description = "월드컵 내용", maxLength = 20)
    @Size(max = 20)
    String description,

    @Schema(description = "월드컵 공개 타입")
    @NotBlank(message = "월드컵 공개 여부: 필수 값")
    VisibleType visibleType,

    @Schema(description = "월드컵 컨텐츠 리스트")
    List<Contents> contentsList
) {
    @Schema
    public record Contents(
            @Schema(description = "월드컵 컨텐츠 식별자")
            Long contentsId,

            @Schema(description = "월드컵 컨텐츠 이름")
            @NotBlank(message = "월드컵 컨텐츠 이름: 필수 값")
            String name,

            @Schema(description = "월드컵 컨텐츠 파일명")
            @NotBlank(message = "월드컵 컨텐츠 파일 이름: 필수 값")
            String filePullName
    ) {

    }
}