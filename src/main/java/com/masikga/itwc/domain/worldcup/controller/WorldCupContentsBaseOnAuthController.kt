package com.masikga.itwc.domain.worldcup.controller

import com.masikga.itwc.common.error.CustomErrorResponse
import com.masikga.itwc.common.web.CustomAuthentication
import com.masikga.itwc.common.web.RestApiResponse
import com.masikga.itwc.common.web.memberresolver.MemberDto
import com.masikga.itwc.domain.worldcup.controller.request.CreateWorldCupContentsRequest
import com.masikga.itwc.domain.worldcup.controller.request.UpdateWorldCupContentsRequest
import com.masikga.itwc.domain.worldcup.service.WorldCupBasedOnAuthService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.NO_CONTENT
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.util.*

@Tag(
    name = "WorldCup based on auth",
    description = "사용자의 인증이 필수적인 월드컵 관련 API"
)
@Validated
@RestController
@RequestMapping("/api/world-cups/me")
class WorldCupContentsBaseOnAuthController(
    private val worldCupBasedOnAuthService: WorldCupBasedOnAuthService
) {

    @Operation(
        summary = "월드컵 컨텐츠 생성",
        parameters = [Parameter(name = "worldCupId", description = "생성하고 싶은 컨텐츠를 포함한 월드컵", required = true)],
        description = "월드컵 관리페이지에서 표시되는 월드컵 리스트 조회",
        security = [SecurityRequirement(name = "Authorization")],
        responses = [ApiResponse(
            responseCode = "400",
            description = "월드컵 게임 작성자가 아님",
            content = arrayOf(
                Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = CustomErrorResponse::class)
                )
            )
        ), ApiResponse(
            responseCode = "404",
            description = "존재하지 않는 월드컵",
            content = arrayOf(
                Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = CustomErrorResponse::class)
                )
            )
        )]
    )
    @ResponseStatus(CREATED)
    @PostMapping("/{worldCupId}/contents")
    fun createMyWorldCupContents(
        @RequestBody request: @Valid CreateWorldCupContentsRequest?,
        @PathVariable worldCupId: Long,
        @Parameter(hidden = true) @CustomAuthentication memberDto: Optional<MemberDto>
    ): RestApiResponse<Nothing> {
        worldCupBasedOnAuthService.createMyWorldCupContents(request, worldCupId, memberDto.get().id)
        return RestApiResponse(1, "게임 생성", null)
    }

    @Operation(
        summary = "월드컵 관리페이지에서 표시되는 월드컵 게임 컨텐츠 제거",
        parameters = [Parameter(name = "worldCupId", description = "삭제하고 싶은 컨텐츠의 월드컵 식별자", required = true), Parameter(
            name = "contentsId",
            description = "삭제하고 싶은 컨텐츠의 월드컵 컨텐츠 식별자",
            required = true
        )],
        security = [SecurityRequirement(name = "Authorization")],
        responses = [ApiResponse(
            responseCode = "400",
            description = "월드컵 게임 작성자가 아님",
            content = arrayOf(
                Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = CustomErrorResponse::class)
                )
            )
        ), ApiResponse(
            responseCode = "404",
            description = "[존재하지 않는 월드컵, 존재하지 않는 월드컵 컨텐츠]",
            content = arrayOf(
                Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = CustomErrorResponse::class)
                )
            )
        )]
    )
    @ResponseStatus(NO_CONTENT)
    @DeleteMapping("/{worldCupId}/contents/{contentsId}")
    fun deleteMyWorldCupContents(
        @PathVariable worldCupId: Long,
        @PathVariable contentsId: Long,
        @Parameter(hidden = true) @CustomAuthentication memberDto: Optional<MemberDto>
    ): RestApiResponse<Long> {
        val deletedContentsId = worldCupBasedOnAuthService.deleteMyWorldCupContents(
            worldCupId, contentsId,
            memberDto.get().id
        )
        return RestApiResponse(1, "게임 삭제", deletedContentsId)
    }


    @Operation(
        summary = "월드컵 컨텐츠 1건 수정",
        parameters = [Parameter(name = "worldCupId", description = "수정하고 싶은 컨텐츠를 포함한 월드컵", required = true), Parameter(
            name = "contentsId",
            description = "수정하고 싶은 컨텐츠 식별자",
            required = true
        )],
        description = "월드컵 관리페이지에서 표시되는 월드컵 리스트 조회",
        security = [SecurityRequirement(name = "Authorization")],
        responses = [ApiResponse(
            responseCode = "400",
            description = "월드컵 게임 작성자가 아님",
            content = arrayOf(
                Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = CustomErrorResponse::class)
                )
            )
        ), ApiResponse(
            responseCode = "404",
            description = "[존재하지 않는 월드컵, 존재하지 않는 월드컵 컨텐츠]",
            content = arrayOf(
                Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = CustomErrorResponse::class)
                )
            )
        )]
    )
    @ResponseStatus(NO_CONTENT)
    @PutMapping("/{worldCupId}/contents/{contentsId}")
    fun updateMyWorldCupContents(
        @RequestBody request: @Valid UpdateWorldCupContentsRequest?,
        @PathVariable worldCupId: Long,
        @PathVariable contentsId: Long,
        @Parameter(hidden = true) @CustomAuthentication memberDto: Optional<MemberDto>
    ): RestApiResponse<Long> {
        val updateContentsId = worldCupBasedOnAuthService.updateMyWorldCupContents(
            request, worldCupId, contentsId,
            memberDto.get().id
        )
        return RestApiResponse(1, "게임 수정", updateContentsId)
    }
}
