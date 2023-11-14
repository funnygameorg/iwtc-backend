package com.example.demo.domain.worldcup.controller.response;

import com.example.demo.domain.worldcup.model.WorldCupGameContents;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record ClearWorldCupGameResponse(
        @Schema(description = "1위 컨텐츠 이름")
        String firstWinnerName,
        @Schema(description = "2위 컨텐츠 이름")
        String secondWinnerName,
        @Schema(description = "3, 4위 컨텐츠 이름")
        String thirdWinnerName,
        @Schema(description = "3, 4위 컨텐츠 이름")
        String fourthWinnerName
) {

        public static ClearWorldCupGameResponse build(
                List<WorldCupGameContents> contents
        ) {
                return new ClearWorldCupGameResponse(
                        contents.get(0).getName(),
                        contents.get(1).getName(),
                        contents.get(2).getName(),
                        contents.get(3).getName()
                );
        }
}