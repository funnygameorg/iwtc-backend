package com.example.demo.domain.worldcup.controller.response;

import com.example.demo.domain.worldcup.model.WorldCupGameContents;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record ClearWorldCupGameResponse(

        @Schema(description = "1위 컨텐츠 정보")
        WinnerSummary firstWinner,

        @Schema(description = "2위 컨텐츠 정보")
        WinnerSummary secondWinner,

        @Schema(description = "3, 4위 컨텐츠 정보")
        WinnerSummary thirdWinner,

        @Schema(description = "4, 4위 컨텐츠 정보")
        WinnerSummary fourthWinner

) {


        public record WinnerSummary(
                @Schema(description = "컨텐츠 이름")
                String contentsName,

                @Schema(description = "컨텐츠 아이디")
                Long contentsId,

                @Schema(description = "미디어 파일 아이디")
                Long mediaFileId

        ) { }




        public static ClearWorldCupGameResponse build(
                List<WorldCupGameContents> contents
        ) {
                return new ClearWorldCupGameResponse(

                        new WinnerSummary(
                                contents.get(0).getName(),
                                contents.get(0).getId(),
                                contents.get(0).getMediaFile().getId()
                        ),
                        new WinnerSummary(
                                contents.get(1).getName(),
                                contents.get(1).getId(),
                                contents.get(1).getMediaFile().getId()
                        ),
                        new WinnerSummary(
                                contents.get(2).getName(),
                                contents.get(2).getId(),
                                contents.get(2).getMediaFile().getId()
                        ),
                        new WinnerSummary(
                                contents.get(3).getName(),
                                contents.get(3).getId(),
                                contents.get(3).getMediaFile().getId()
                        )
                );

        }
}