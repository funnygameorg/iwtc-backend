package com.example.demo.domain.worldcup.controller;

import com.example.demo.common.web.auth.CustomAuthentication;
import com.example.demo.common.web.memberresolver.MemberDto;
import com.example.demo.domain.member.model.Member;
import com.example.demo.domain.worldcup.controller.request.CreateWorldCupContentsRequest;
import com.example.demo.domain.worldcup.controller.request.CreateWorldCupRequest;
import com.example.demo.domain.worldcup.controller.response.GetMyWorldCupSummariesResponse;
import com.example.demo.domain.worldcup.controller.response.GetMyWorldCupSummaryRanksResponse;
import com.example.demo.domain.worldcup.controller.response.GetWorldCupContentsResponse;
import com.example.demo.domain.worldcup.controller.response.GetWorldCupResponse;
import com.example.demo.common.error.CustomErrorResponse;
import com.example.demo.common.web.RestApiResponse;
import com.example.demo.domain.worldcup.controller.vo.WorldCupSort;
import com.example.demo.domain.worldcup.service.WorldCupBasedOnAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@Tag(
        name = "WorldCup based on auth",
        description = "사용자의 인증이 필수적인 월드컵 관련 API"
)
@RestController
@RequestMapping("/api/world-cups/me")
@RequiredArgsConstructor
public class WorldCupBasedOnAuthController {

    private final WorldCupBasedOnAuthService worldCupBasedOnAuthService;

    @Operation(
            summary = "이상형 월드컵 삭제/수정에 사용되는 컨텐츠 조회",
            description = "월드컵 수정/생성 페이지에서 사용되는 컨텐츠 리스트를 조회합니다.",
            security = @SecurityRequirement(name = "access-token"),
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
                            responseCode = "400",
                            description = "사용자가 작성한 월드컵 게임이 아님",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomErrorResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "인증 실패",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomErrorResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "존재하지 않는 월드컵 게임",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomErrorResponse.class))
                    ),
            }
    )
    @GetMapping("/{worldCupId}/contents")
    @ResponseStatus(HttpStatus.OK)
    public RestApiResponse<List<GetWorldCupContentsResponse>> getMyWorldCupGameContentsList(
            @PathVariable
            long worldCupId,

            @Parameter(hidden = true)
            @CustomAuthentication
            Optional<MemberDto> memberDto
    ) {
        return new RestApiResponse(
                1,
                "조회 성공",
                worldCupBasedOnAuthService.getMyWorldCupGameContents(worldCupId, memberDto.get().getId())
        );
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
            @PathVariable long worldCupId
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
            @RequestParam(name = "sort") WorldCupSort sort
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
            summary = "자신의 월드컵 1개 수정",
            description = "자신의 월드컵 1개를 수정 합니다.",
            parameters = {
                    @Parameter(
                            name = "worldCupId",
                            description = "조회하고 싶은 컨텐츠의 월드컵 식별자",
                            required = true
                    )
            },
            security = @SecurityRequirement(name = "Authorization"),
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "월드컵 수정",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "존재하지 않는 월드컵",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "사용자가 작성한 월드컵이 아님",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomErrorResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "중복된 월드컵 타이틀을 사용하려고 함",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomErrorResponse.class))
                    )
            }
    )
    @ResponseStatus(NO_CONTENT)
    @PutMapping("/{worldCupId}")
    public RestApiResponse<Object> putMyWorldCup(
            @Valid
            @RequestBody
            CreateWorldCupRequest request,

            @Parameter(hidden = true)
            @CustomAuthentication
            Optional<MemberDto> memberDto,

            @PathVariable(required = false)
            Long worldCupId
    ) {

        worldCupBasedOnAuthService.putMyWorldCup(
                request,
                worldCupId,
                memberDto.get().getId()
        );

        return new RestApiResponse(
                1,
                "게임 수정",
                null
        );

    }


    @Operation(
            summary = "자신의 월드컵 1개 생성",
            description = "월드컵 1개를 생성 합니다.",
            parameters = {
                    @Parameter(
                            name = "worldCupId",
                            description = "조회하고 싶은 컨텐츠의 월드컵 식별자",
                            required = true
                    )
            },
            security = @SecurityRequirement(name = "Authorization"),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "월드컵 생성",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "중복된 월드컵 타이틀을 사용하려고 함",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomErrorResponse.class))
                    )
            }
    )
    @ResponseStatus(CREATED)
    @PostMapping
    public RestApiResponse<Object> createMyWorldCup(
            @Valid
            @RequestBody
            CreateWorldCupRequest request,

            @Parameter(hidden = true)
            @CustomAuthentication
            Optional<MemberDto> memberDto
    ) {

        long worldCupId = worldCupBasedOnAuthService.createMyWorldCup(
                request,
                memberDto.get().getId()
        );

        return new RestApiResponse(
                1,
                "게임 생성",
                worldCupId
        );

    }

    @ResponseStatus(CREATED)
    @PostMapping("/{worldCupId}/contents")
    public RestApiResponse<Object> createMyWorldCupContents(
            @Valid
            @RequestBody
            CreateWorldCupContentsRequest request,

            @PathVariable
            long worldCupId,

            @Parameter(hidden = true)
            @CustomAuthentication
            Optional<MemberDto> memberDto
    ) {

        worldCupBasedOnAuthService.createMyWorldCupContents(
                request,
                worldCupId,
                memberDto.get().getId()
        );

        return new RestApiResponse(
                1,
                "게임 생성",
                null
        );

    }
}