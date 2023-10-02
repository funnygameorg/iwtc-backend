package com.example.demo.domain.idealtype.controller;

import com.example.demo.domain.idealtype.controller.dto.response.GetWorldCupsResponse;
import com.example.demo.domain.idealtype.controller.vo.WorldCupDateRange;
import com.example.demo.domain.idealtype.controller.vo.WorldCupSort;
import com.example.demo.global.web.RestApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "WorldCup", description = "월드컵 게임 제공 API")
@RestController
@RequestMapping("/ideal-type-world-cups")
public class WorldCupController {

    @Operation(
            summary = "전체 월드컵 리스트 조회(페이징)",
            description = "월드컵 리스트 전체를 조회합니다.",
            parameters = {
                    @Parameter(
                            name = "keyword",
                            description = "조회하고 싶은 Keyword"
                    ),
                    @Parameter(
                            name = "sort",
                            description = "정렬 기준"
                    ),
                    @Parameter(
                            name = "dateRange",
                            description = "게임 생성 기간"
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
    public RestApiResponse<Page<GetWorldCupsResponse>> getWorldCups(
            @RequestParam(name = "sort", defaultValue = "POPULARITY") WorldCupSort sort,
            @RequestParam(name = "dateRange", defaultValue = "ALL") WorldCupDateRange dateRange,
            @RequestParam(name = "keyword", required = false) String keyword,
            @PageableDefault(size = 25, direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return new RestApiResponse(1, "", null);
    }

}
