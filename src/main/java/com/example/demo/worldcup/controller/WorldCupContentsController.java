package com.example.demo.worldcup.controller;

import com.example.demo.worldcup.controller.dto.response.GetMyWorldCupsAllContentsResponse;
import com.example.demo.worldcup.controller.dto.response.GetWorldCupPlayContentsResponse;
import com.example.demo.common.error.CustomErrorResponse;
import com.example.demo.common.web.RestApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "WorldCup contents", description = "월드컵 게임 컨텐츠 제공 API")
@RestController
@RequestMapping("/api/world-cups")
public class WorldCupContentsController {

    @Operation(
            summary = "월드컵 컨텐츠 생성 썸네일 컴포넌트 조회용",
            description = "월드컵 리스트 전체를 조회합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "조회 성공"
                    )
            }
    )
    @GetMapping("/{worldCupId}/contents-media-files")
    @ResponseStatus(HttpStatus.OK)
    public RestApiResponse<GetMyWorldCupsAllContentsResponse> getMyWorldCupsAllContents() {
        return new RestApiResponse(1, "", null);
    }

    @Operation(
            summary = "이상형 월드컵 게임 진행에 필요한 정보 제공",
            description = "월드컵 리스트 전체를 조회합니다.",
            parameters = {
                    @Parameter(
                            name = "round",
                            description = "게임 플레이 round",
                            required = true
                    ),
                    @Parameter(
                            name = "ignoreContentIds",
                            description = "조회에서 제외하는 컨텐츠 식별자"
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
                    )
            }
    )
    @GetMapping("/{worldCupId}/play-contents")
    @ResponseStatus(HttpStatus.OK)
    public RestApiResponse<GetWorldCupPlayContentsResponse> getPlayContents(
            @RequestParam(name = "round") int round,
            @RequestParam(name = "ignoreContentIds", required = false) List<Long> ignoreContentIds
    ) {
        return new RestApiResponse(1, "", null);
    }
}
