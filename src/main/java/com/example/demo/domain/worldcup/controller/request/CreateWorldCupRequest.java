package com.example.demo.domain.worldcup.controller.request;

import com.example.demo.domain.worldcup.model.WorldCupGame;
import com.example.demo.domain.worldcup.model.vo.VisibleType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.List;

@Schema
@Builder
public record CreateWorldCupRequest (
    @Schema(description = "월드컵 이름")
    @NotBlank(message = "월드컵 제목: 필수 값")
    String title,

    @Schema(description = "월드컵 내용", maxLength = 100)
    @Size(max = 100)
    String description,

    @Schema(description = "월드컵 공개 타입")
    @NotNull(message = "월드컵 공개 여부: 필수 값")
    VisibleType visibleType

) {


    public WorldCupGame toEntity(long memberId) {
        return WorldCupGame.createNewGame(
                title,
                description,
                visibleType,
                memberId
        );
    }
}