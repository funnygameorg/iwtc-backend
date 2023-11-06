package com.example.demo.domain.worldcup.controller;

import com.example.demo.common.web.auth.CustomAuthentication;
import com.example.demo.common.web.memberresolver.MemberDto;
import com.example.demo.domain.worldcup.controller.request.ClearWorldCupGameRequest;
import com.example.demo.domain.worldcup.controller.response.GetWorldCupPlayContentsResponse;
import com.example.demo.common.error.CustomErrorResponse;
import com.example.demo.common.web.RestApiResponse;
import com.example.demo.domain.worldcup.controller.response.GetAvailableGameRoundsResponse;
import com.example.demo.domain.worldcup.service.WorldCupGameContentsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.*;

@Tag(name = "WorldCup contents", description = "월드컵 게임 컨텐츠 제공 API")
@RestController
@RequestMapping("/api/world-cups")
@RequiredArgsConstructor
public class WorldCupContentsController {

    private final WorldCupGameContentsService worldCupGameContentsService;

    @Operation(
            summary = "이상형 월드컵 게임 진행에 필요한 정보 제공",
            description = "이상형 월드컵 게임을 위한 이상형 리스트를 제공합니다.",
            parameters = {
                    @Parameter(
                            name = "worldCupId",
                            description = "현재 진행중인 월드컵 게임 id",
                            required = true
                    ),
                    @Parameter(
                            name = "currentRound",
                            description = "현재 게임 진행 라운드",
                            required = true
                    ),
                    @Parameter(
                            name = "divideContentsSizePerRequest",
                            description = "컨텐츠 토막 개수 [ 최소 : 1, 최대 : 4 ]",
                            required = true
                    ),
                    @Parameter(
                            name = "alreadyPlayedContentsIds",
                            description = "이미 사용자가 선택한 이상형 컨텐츠 리스트, 해당 컨텐츠 리스트를 제외하고 조회한다."
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "조회 성공"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "1 : 이미 플레이한 게임 컨텐츠를 조회, 2: 요청한 컨텐츠 사이즈와 실제 조회 사이즈가 다름, 3:플레이 불가능 라운드",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomErrorResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "존재하지 않는 월드컵 게임",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomErrorResponse.class))
                    )
            }
    )
    @GetMapping("/{worldCupId}/contents")
    @ResponseStatus(OK)
    public RestApiResponse<GetWorldCupPlayContentsResponse> getPlayContents(
            @PathVariable
            Long worldCupId,

            @RequestParam(name = "currentRound")
            int currentRound,

            @Size(min = 1, max = 4)
            @RequestParam(name = "divideContentsSizePerRequest")
            int divideContentsSizePerRequest,

            @RequestParam(name = "alreadyPlayedContentsIds", required = false, defaultValue = "")
            List<Long> alreadyPlayedContentsIds
    ) {
        return new RestApiResponse(
                1,
                "컨텐츠 조회 성공",
                worldCupGameContentsService.getPlayContents(worldCupId, currentRound, divideContentsSizePerRequest, alreadyPlayedContentsIds)
        );
    }

    @Operation(
            summary = "플레이 가능한 월드컵 게임 라운드 수 조회",
            description = "월드컵 게임이 포함하는 컨텐츠 수에서 진행할 수 있는 라운드 수를 제공",
            parameters = {
                    @Parameter(
                            name = "worldCupId",
                            description = "라운드 수 조회를 원하는 월드컵 게임 id",
                            required = true
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "조회 성공"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "게임 라운드 조회 불가, 게임을 진행할 수 있는 이상형 컨텐츠 부족",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomErrorResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "존재하지 않는 월드컵 게임",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomErrorResponse.class))
                    )
            }
    )
    @GetMapping("/{worldCupId}/available-rounds")
    @ResponseStatus(OK)
    public RestApiResponse<GetAvailableGameRoundsResponse> getAvailableGameRounds(
            @PathVariable Long worldCupId
    ) {
        return new RestApiResponse(
                1,
                "플레이 가능한 라운드 조회 성공",
                worldCupGameContentsService.getAvailableGameRounds(worldCupId)
        );
    }

    @Operation(
            summary = "월드컵 게임 종료",
            description = "월드컵 게임을 종료, 결과를 저장",
            parameters = {
                    @Parameter(
                            name = "worldCupId",
                            description = "라운드 수 조회를 원하는 월드컵 게임 id"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "게임 결과 저장 완료"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "존재하지 않는 월드컵 게임",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomErrorResponse.class))
                    )
            }
    )
    @PostMapping("/{worldCupId}/clear")
    @ResponseStatus(CREATED)
    public RestApiResponse clearWorldCupGame(
            @PathVariable
            long worldCupId,

            @RequestBody
            ClearWorldCupGameRequest request
    ) {
        worldCupGameContentsService.clearWorldCupGame(worldCupId, request);
        return RestApiResponse.builder()
                .data(null)
                .code(1)
                .message("게임 결과 생성")
                .build();
    }
}
