package com.masikga.worldcupgame.domain.worldcup.controller;

import com.masikga.worldcupgame.common.web.RestApiResponse;
import com.masikga.worldcupgame.domain.worldcup.controller.response.GetWorldCupsResponse;
import com.masikga.worldcupgame.domain.worldcup.controller.validator.worldcup.VerifyWorldCupKeyword;
import com.masikga.worldcupgame.domain.worldcup.controller.vo.WorldCupDateRange;
import com.masikga.worldcupgame.domain.worldcup.service.WorldCupGameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "WorldCup", description = "월드컵 게임 제공 API")
@Validated
@RestController
@RequestMapping("/api/world-cups")
@RequiredArgsConstructor
public class WorldCupGameController {

    private final WorldCupGameService worldCupGameService;

    @Operation(
            summary = "전체 월드컵 리스트 조회(페이징)",
            description = "월드컵 리스트 전체를 조회합니다. sort 값 : WorldCupSort 참조",
            parameters = {
                    @Parameter(
                            name = "keyword",
                            description = "조회하고 싶은 Keyword"
                    ),
                    @Parameter(
                            name = "dateRange",
                            description = "게임 생성 기간 [ALL, YEAR, MONTH, WEEK] / 기본 값 : ALL"
                    ),
                    @Parameter(
                            name = "direction",
                            description = "정렬 방향 [ASC, DESC] / 기본 : DESC"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "조회 성공"
                    )
            }
    )
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public com.masikga.worldcupgame.common.web.RestApiResponse<Page<GetWorldCupsResponse>> getWorldCups(

            @RequestParam(name = "dateRange", required = false, defaultValue = "ALL")
            WorldCupDateRange dateRange,

            @VerifyWorldCupKeyword
            @RequestParam(name = "keyword", required = false)
            String worldCupKeyword,

            @RequestParam(name = "memberId", required = false)
            Long memberId,

            @PageableDefault(
                    size = 25,
                    direction = Sort.Direction.DESC,
                    sort = {"id"}
            ) Pageable pageable
    ) {

        var result = worldCupGameService.findWorldCupByPageable(pageable, dateRange, worldCupKeyword, memberId);
        return new RestApiResponse(1, "월드컵 페이지 조회 성공", result);

    }

}
