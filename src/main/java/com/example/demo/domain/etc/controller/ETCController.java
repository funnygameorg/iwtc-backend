package com.example.demo.domain.etc.controller;
import com.example.demo.domain.etc.controller.dto.request.WriteCommentRequest;

import com.example.demo.domain.etc.controller.dto.response.CreateAccessTokenResponse;
import com.example.demo.domain.etc.controller.dto.response.MediaFileResponse;
import com.example.demo.global.jwt.JwtService;
import com.example.demo.global.web.RestApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "ETC", description = "서비스의 여러 기능에 공통적으로 사용되는 API")
@RestController
@RequiredArgsConstructor
class ETCController {
    private final JwtService jwtService;

    @Operation(
            summary = "미디어 파일 조회",
            description = "파라미터로 받은 식별자에 해당하는 미디어 파일을 조회합니다. (다건과 단건을 모두 사용)",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "조회 성공"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "미디어 파일 없음",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))
                    )
            }
    )
    @GetMapping("/media-files/{mediaFileIds}")
    @ResponseStatus(HttpStatus.OK)
    public RestApiResponse<List<MediaFileResponse>> getMediaFiles(
            @PathVariable List<Long> mediaFileIds
    ) {
        return new RestApiResponse(1, "", null);
    }

    @Operation(
            summary = "컨텐츠(여러 게임, 아이돌...)에 의견 작성",
            description = "서비스에 사용되는 컨텐츠의 의견 작성",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "의견 작성",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "존재하지 않는 컨텐츠",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))
                    )
            }
    )
    @PostMapping("/contents/{contentsId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public RestApiResponse<WriteCommentRequest> writeComment(
            @PathVariable Long contentsId,
            @Valid @RequestBody WriteCommentRequest request
    ) {
        return new RestApiResponse(1, "", null);
    }

    // TODO: jwt 라이브러리 예외 응답 추가하기
    @Operation(
            summary = "새로운 액세스 토큰 반환",
            description = "리프레시 토큰을 받아 새로운 액세스 토큰을 반환합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "액세스 토큰 생성"
                    )
            }
    )
    @PostMapping("/new-access-token")
    @ResponseStatus(HttpStatus.OK)
    public RestApiResponse<CreateAccessTokenResponse> createAccessToken(
            @RequestHeader("refresh-token") String refreshToken
    ) {
        String newAccessToken = jwtService.createAccessTokenByRefreshToken(refreshToken);
        return new RestApiResponse<CreateAccessTokenResponse>(
                1,
                "엑세스 토큰 생성",
                new CreateAccessTokenResponse(newAccessToken)
        );
    }
}
