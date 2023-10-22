package com.example.demo.worldcup.controller;

import com.example.demo.worldcup.controller.dto.response.GetWorldCupsResponse;
import com.example.demo.worldcup.controller.vo.WorldCupDateRange;
import com.example.demo.common.web.RestApiResponse;
import com.example.demo.worldcup.model.projection.FindWorldCupGamePageProjection;
import com.example.demo.worldcup.service.WorldCupGameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "WorldCup", description = "월드컵 게임 제공 API")
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
                            description = "게임 생성 기간",
                            required = true
                    ),
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
    public RestApiResponse<Page<GetWorldCupsResponse>> getWorldCups(
            @RequestParam(name = "dateRange", defaultValue = "ALL") WorldCupDateRange dateRange,
            @RequestParam(name = "keyword", required = false) String worldCupKeyword,
            @PageableDefault(
                    size = 25,
                    direction = Sort.Direction.DESC,
                    sort = { "id" }
            ) Pageable pageable
    ) {
        Page<FindWorldCupGamePageProjection> result = worldCupGameService.findWorldCupByPageable(
                pageable,
                dateRange,
                worldCupKeyword
        );
        return new RestApiResponse(1, "월드컵 페이지 조회 성공", result);
    }

}
