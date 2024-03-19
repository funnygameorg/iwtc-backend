package com.masikga.worldcupgame.domain.worldcup.controller;

import com.google.common.primitives.Longs;
import com.masikga.error.CustomErrorResponse;
import com.masikga.worldcupgame.common.web.RestApiResponse;
import com.masikga.worldcupgame.domain.worldcup.controller.request.ClearWorldCupGameRequest;
import com.masikga.worldcupgame.domain.worldcup.controller.response.ClearWorldCupGameResponse;
import com.masikga.worldcupgame.domain.worldcup.controller.response.GetAvailableGameRoundsResponse;
import com.masikga.worldcupgame.domain.worldcup.controller.response.GetGameResultContentsListResponse;
import com.masikga.worldcupgame.domain.worldcup.controller.response.GetWorldCupPlayContentsResponse;
import com.masikga.worldcupgame.domain.worldcup.service.WorldCupGameContentsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Tag(name = "WorldCup contents", description = "월드컵 게임 컨텐츠 제공 API ")
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
                            name = "sliceContents",
                            description = "컨텐츠 토막 개수 [ 최소 : 1, 최대 : 4 ]",
                            required = true
                    ),
                    @Parameter(
                            name = "excludeContentsIds",
                            description = "이미 사용자가 선택 혹은 탈락한 이상형 컨텐츠 리스트, 해당 컨텐츠 리스트를 제외하고 조회한다."
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
    public com.masikga.worldcupgame.common.web.RestApiResponse<GetWorldCupPlayContentsResponse> getPlayContents(
            @PathVariable
            long worldCupId,

            @RequestParam(name = "currentRound")
            int currentRound,

            @Size(min = 1, max = 4)
            @RequestParam(name = "sliceContents")
            int sliceContents,

            @RequestParam(name = "excludeContentsIds", required = false)
            long[] excludeContentsIds
    ) {

        var response = worldCupGameContentsService.getPlayContents(worldCupId, currentRound, sliceContents,
                boxedPrimitiveLongs(excludeContentsIds));
        return new com.masikga.worldcupgame.common.web.RestApiResponse(1, "컨텐츠 조회 성공", response);

    }

    @Operation(
            summary = "이상형 월드컵 게임의 모든 컨텐츠 조회 (랭크 정렬)",
            description = "이상형 월드컵 게임 결과 페이지에서 사용됩니다.",
            parameters = {
                    @Parameter(
                            name = "worldCupId",
                            description = "확인하고 싶은 월드컵",
                            required = true
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "조회 성공"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "존재하지 않는 월드컵 게임",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomErrorResponse.class))
                    )
            }
    )
    @GetMapping("/{worldCupId}/game-result-contents")
    @ResponseStatus(OK)
    public com.masikga.worldcupgame.common.web.RestApiResponse<List<GetGameResultContentsListResponse>> getGameResultContentsList(
            @PathVariable Long worldCupId
    ) {

        var response = worldCupGameContentsService.getGameResultContentsList(worldCupId);
        return new com.masikga.worldcupgame.common.web.RestApiResponse(1, "게임 결과 컨텐츠 리스트 조회 성공", response);

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
    public com.masikga.worldcupgame.common.web.RestApiResponse<GetAvailableGameRoundsResponse> getAvailableGameRounds(

            @PathVariable long worldCupId

    ) {

        var response = worldCupGameContentsService.getAvailableGameRounds(worldCupId);
        return new com.masikga.worldcupgame.common.web.RestApiResponse(1, "플레이 가능한 라운드 조회 성공", response);

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
    public com.masikga.worldcupgame.common.web.RestApiResponse<ClearWorldCupGameResponse> clearWorldCupGame(

            @PathVariable
            long worldCupId,

            @RequestBody
            ClearWorldCupGameRequest request

    ) {

        var response = worldCupGameContentsService.clearWorldCupGame(worldCupId, request);
        return new RestApiResponse(1, "게임 결과 생성", response);

    }

    private List<Long> boxedPrimitiveLongs(long[] primitiveLongs) {
        long[] nullableLongs = primitiveLongs != null ? primitiveLongs : new long[0];
        return Longs.asList(nullableLongs);
    }

}
