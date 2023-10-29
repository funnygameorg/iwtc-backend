package com.example.demo.domain.worldcup.controller;

import com.example.demo.domain.worldcup.controller.request.CreateWorldCupRequest;
import com.example.demo.domain.worldcup.controller.response.GetMyWorldCupSummariesResponse;
import com.example.demo.domain.worldcup.controller.response.GetMyWorldCupSummaryRanksResponse;
import com.example.demo.domain.worldcup.controller.response.GetWorldCupContentsResponse;
import com.example.demo.domain.worldcup.controller.response.GetWorldCupResponse;
import com.example.demo.domain.worldcup.model.vo.MyWorldCupRankSort;
import com.example.demo.common.error.CustomErrorResponse;
import com.example.demo.common.web.RestApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "WorldCup based on auth",
        description = "사용자의 인증이 필수적인 월드컵 관련 API"
)
@RestController
@RequestMapping("/api/world-cups/me")
public class WorldCupBasedOnAuthController {

    @Operation(
            summary = "이상형 월드컵 삭제/수정에 사용되는 이미지 조회(디테일)",
            description = "자신의 월드컵 게임 수정 용도에 사용하는 API, 월드컵에 포함된 컨텐츠의 자세한 내용을 조회합니다.",
            security = @SecurityRequirement(name = "Authorization"),
            parameters = {
                    @Parameter(
                            name = "worldCupId",
                            description = "조회하고 싶은 컨텐츠의 월드컵 식별자"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "조회 성공"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "인증 실패",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomErrorResponse.class))
                    )
            }
    )
    @GetMapping("/{worldCupId}/contents-images")
    @ResponseStatus(HttpStatus.OK)
    public RestApiResponse<List<GetWorldCupContentsResponse>> getContentsImages(
            @PathVariable("worldCupId") Long worldCupId
    ) {
        return new RestApiResponse(1, "", null);
    }

    @Operation(
            summary = "나의 월드컵 1개 조회 (생성, 수정용도)",
            description = "자신의 월드컵 게임 1개에 대하여 조회합니다. (생성, 수정용도)",
            security = @SecurityRequirement(name = "Authorization"),
            parameters = {
                    @Parameter(
                            name = "worldCupId",
                            description = "조회하고 싶은 컨텐츠의 월드컵 식별자",
                            required = true
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "조회 성공"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "인증 실패",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomErrorResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "월드컵 없음",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomErrorResponse.class))
                    )
            }
    )
    @GetMapping("/{worldCupId}")
    @ResponseStatus(HttpStatus.OK)
    public RestApiResponse<GetWorldCupResponse> getWorldCup(
            @PathVariable("worldCupId") Long worldCupId
    ) {
        return new RestApiResponse(1, "", null);
    }

    @Operation(
            summary = "마이페이지 월드컵 리스트 (순위 정렬)",
            description = "자신의 월드컵을 [조회, 댓글순...]같은 값을 사용하여 정렬하여 조회합니다.",
            security = @SecurityRequirement(name = "Authorization"),
            parameters = {
                    @Parameter(
                            name = "sort",
                            description = "순위 정렬 근거 [LATEST, POPULARITY]",
                            required = true
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "조회 성공"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "인증 실패",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomErrorResponse.class))
                    )
            }
    )
    @GetMapping("/summary-ranks")
    @ResponseStatus(HttpStatus.OK)
    public RestApiResponse<List<GetMyWorldCupSummaryRanksResponse>> getMyWorldCupSummaryRanks(
            @RequestParam(name = "sort") MyWorldCupRankSort sort
    ) {
        return new RestApiResponse(1, "", null);
    }

    @Operation(
            summary = "마이페이지 리스트용",
            description = "자신의 월드컵의 리스트를 간략화된 정보 형태로 조회합니다.",
            security = @SecurityRequirement(name = "Authorization"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "조회 성공"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "인증 실패",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomErrorResponse.class))
                    )
            }
    )
    @GetMapping("/summaries")
    @ResponseStatus(HttpStatus.OK)
    public RestApiResponse<List<GetMyWorldCupSummariesResponse>> getMyWorldCupSummaries() {
        return new RestApiResponse(1, "", null);
    }

    @Operation(
            summary = "자신의 월드컵 1개 생성/수정",
            description = "자신의 월드컵 1개를 생성/수정 합니다.",
            security = @SecurityRequirement(name = "Authorization"),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "월드컵 생성",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "204",
                            description = "월드컵 수정",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "인증 실패",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomErrorResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "월드컵 없음",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomErrorResponse.class))
                    )
            }
    )
    @PutMapping
    public RestApiResponse<Object> replaceWorldCup(
            @Valid @RequestBody CreateWorldCupRequest request
    ) {
        return new RestApiResponse(1, "", null);
    }
}