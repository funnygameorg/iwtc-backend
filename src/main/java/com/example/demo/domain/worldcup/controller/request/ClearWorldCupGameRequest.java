package com.example.demo.domain.worldcup.controller.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Builder
public record ClearWorldCupGameRequest (
        @Schema(description = "1위 컨텐츠 id")
        int firstWinnerContentsId,
        @Schema(description = "2위 컨텐츠 id")
        int secondWinnerContentsId,
        @Schema(description = "3, 4위 컨텐츠 id")
        int thirdWinnerContentsId,
        @Schema(description = "3, 4위 컨텐츠 id")
        int fourthWinnerContentsId
) {

    // 요청에 포함된 순위권 이상형을 List로 반환한다.
    @JsonIgnore
    public List<Long> getWinnerIds() {
        return List.of(
                (long) firstWinnerContentsId,
                (long) secondWinnerContentsId,
                (long) thirdWinnerContentsId,
                (long) fourthWinnerContentsId
        );
    }
}