package com.example.demo.domain.worldcup.controller;

import com.example.demo.domain.worldcup.controller.response.GetMyWorldCupsAllContentsResponse;
import com.example.demo.domain.worldcup.controller.response.GetWorldCupPlayContentsResponse;
import com.example.demo.common.error.CustomErrorResponse;
import com.example.demo.common.web.RestApiResponse;
import com.example.demo.domain.worldcup.controller.response.getAvailableGameRoundsResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@Tag(name = "WorldCup contents", description = "월드컵 게임 컨텐츠 제공 API")
@RestController
@RequestMapping("/api/world-cups")
public class WorldCupContentsController {
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
                            name = "round",
                            description = "현재 게임 진행 라운드",
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
                            description = "플레이 불가능 라운드",
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
            @PathVariable Long worldCupId,
            @RequestParam(name = "round") int round,
            @RequestParam(name = "alreadyPlayedContentsIds", required = false) List<Long> alreadyPlayedContentsIds
    ) {
        return new RestApiResponse(1, "", null);
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
    public RestApiResponse<getAvailableGameRoundsResponse> getAvailableGameRounds(
            @PathVariable Long worldCupId
    ) {
        return new RestApiResponse(
                1,
                "",
                null
        );
    }
}
