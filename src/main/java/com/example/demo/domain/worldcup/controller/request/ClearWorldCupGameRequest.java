package com.example.demo.domain.worldcup.controller.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Builder
public record ClearWorldCupGameRequest (
        int firstWinnerContentsId,
        int secondWinnerContentsId,
        int thirdWinnerContentsId,
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