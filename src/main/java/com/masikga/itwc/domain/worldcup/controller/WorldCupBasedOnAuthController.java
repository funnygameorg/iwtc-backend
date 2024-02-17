package com.masikga.itwc.domain.worldcup.controller;

import com.masikga.itwc.common.error.CustomErrorResponse;
import com.masikga.itwc.common.web.CustomAuthentication;
import com.masikga.itwc.common.web.RestApiResponse;
import com.masikga.itwc.common.web.memberresolver.MemberDto;
import com.masikga.itwc.domain.worldcup.controller.request.CreateWorldCupRequest;
import com.masikga.itwc.domain.worldcup.controller.response.GetMyWorldCupResponse;
import com.masikga.itwc.domain.worldcup.controller.response.GetWorldCupResponse;
import com.masikga.itwc.domain.worldcup.service.WorldCupBasedOnAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.*;

@Tag(
        name = "WorldCup based on auth",
        description = "사용자의 인증이 필수적인 월드컵 관련 API"
)
@Validated
@RestController
@RequestMapping("/api/me/game-manage/world-cups")
@RequiredArgsConstructor
public class WorldCupBasedOnAuthController {

    private final WorldCupBasedOnAuthService worldCupBasedOnAuthService;

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
    @ResponseStatus(OK)
    public RestApiResponse<GetWorldCupResponse> getWorldCup(
            @PathVariable long worldCupId,

            @Parameter(hidden = true)
            @CustomAuthentication
            Optional<MemberDto> memberDto
    ) {

        var response = worldCupBasedOnAuthService.getMyWorldCup(worldCupId, memberDto.get().getId());
        return new RestApiResponse(1, "자신의 월드컵 조회", response);

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
                            responseCode = "400",
                            description = "사용자가 작성한 월드컵이 아님",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomErrorResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "존재하지 않는 월드컵",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponse.class))
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
    public RestApiResponse putMyWorldCup(
            @Valid
            @RequestBody
            CreateWorldCupRequest request,

            @Parameter(hidden = true)
            @CustomAuthentication
            Optional<MemberDto> memberDto,

            @PathVariable(required = false)
            Long worldCupId
    ) {

        worldCupBasedOnAuthService.putMyWorldCup(request, worldCupId, memberDto.get().getId());
        return new RestApiResponse(1, "게임 수정", null);

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
    public RestApiResponse<Long> createMyWorldCup(
            @Valid
            @RequestBody
            CreateWorldCupRequest request,

            @Parameter(hidden = true)
            @CustomAuthentication
            Optional<MemberDto> memberDto
    ) {

        var worldCupId = worldCupBasedOnAuthService.createMyWorldCup(request, memberDto.get().getId());
        return new RestApiResponse(1, "게임 생성", worldCupId);

    }

    @Operation(
            summary = "월드컵 관리페이지에서 표시되는 월드컵 게임 제거",
            parameters = {
                    @Parameter(
                            name = "worldCupId",
                            description = "삭제하고 싶은 컨텐츠의 월드컵 식별자",
                            required = true
                    )
            },
            security = @SecurityRequirement(name = "Authorization"),
            responses = {
                    @ApiResponse(
                            responseCode = "400",
                            description = "월드컵 게임 작성자가 아님",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomErrorResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "[존재하지 않는 월드컵, 존재하지 않는 월드컵 컨텐츠]",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomErrorResponse.class))
                    ),

            }
    )
    @ResponseStatus(NO_CONTENT)
    @DeleteMapping("/{worldCupId}")
    public RestApiResponse<Long> deleteMyWorldCup(

            @PathVariable
            long worldCupId,

            @Parameter(hidden = true)
            @CustomAuthentication
            Optional<MemberDto> memberDto
    ) {

        var deletedContentsId = worldCupBasedOnAuthService.deleteMyWorldCup(worldCupId,
                memberDto.get().getId());
        return new RestApiResponse(1, "게임 삭제", deletedContentsId);

    }

    @Operation(
            summary = "월드컵 관리페이지에서 표시되는 월드컵 리스트 조회",
            description = "월드컵 관리페이지에서 표시되는 월드컵 리스트 조회",
            security = @SecurityRequirement(name = "Authorization")
    )
    @ResponseStatus(OK)
    @GetMapping
    public RestApiResponse<List<GetMyWorldCupResponse>> getMyWorldCups(
            @Parameter(hidden = true)
            @CustomAuthentication
            Optional<MemberDto> memberDto
    ) {

        var response = worldCupBasedOnAuthService.getMyWorldCupList(memberDto.get().getId());
        return new RestApiResponse(1, "자신의 게임 리스트 조회", response);

    }


}