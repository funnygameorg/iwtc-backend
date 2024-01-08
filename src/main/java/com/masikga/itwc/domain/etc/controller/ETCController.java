package com.masikga.itwc.domain.etc.controller;

import static org.springframework.http.HttpStatus.*;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.masikga.itwc.common.error.CustomErrorResponse;
import com.masikga.itwc.common.jwt.JwtService;
import com.masikga.itwc.common.web.CustomAuthentication;
import com.masikga.itwc.common.web.RestApiResponse;
import com.masikga.itwc.common.web.memberresolver.MemberDto;
import com.masikga.itwc.domain.etc.controller.request.CreateAccessTokenRequest;
import com.masikga.itwc.domain.etc.controller.request.WriteCommentRequest;
import com.masikga.itwc.domain.etc.controller.response.CreateAccessTokenResponse;
import com.masikga.itwc.domain.etc.controller.response.GetCommentsListResponse;
import com.masikga.itwc.domain.etc.controller.response.MediaFileResponse;
import com.masikga.itwc.domain.etc.service.CommentService;
import com.masikga.itwc.domain.etc.service.MediaFileService;
import com.masikga.itwc.domain.etc.service.TokenService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "ETC", description = "서비스의 여러 기능에 공통적으로 사용되는 API")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ETCController {
	private final JwtService jwtService;
	private final MediaFileService mediaFileService;
	private final CommentService commentService;
	private final TokenService tokenService;

	@Operation(
		summary = "월드컵 컨텐츠에 댓글 작성",
		responses = {
			@ApiResponse(
				responseCode = "201",
				description = "댓글 작성",
				content = @Content(
					mediaType = "application/json",
					schema = @Schema(implementation = RestApiResponse.class)
				)
			),
			@ApiResponse(
				responseCode = "404",
				description = "[존재하지 않는 월드컵, 존재하지 않는 컨텐츠]",
				content = @Content(
					mediaType = "application/json",
					schema = @Schema(implementation = CustomErrorResponse.class)
				)
			)
		}
	)
	@PostMapping("/world-cups/{worldCupId}/contents/{contentsId}/comments")
	@ResponseStatus(CREATED)
	public RestApiResponse writeComment(

		@PathVariable
		Long worldCupId,

		@PathVariable
		Long contentsId,

		@Valid
		@RequestBody
		WriteCommentRequest request,

		@Parameter(hidden = true)
		@CustomAuthentication(required = false)
		Optional<MemberDto> optionalMemberDto

	) {

		Long writerId = null;

		if (optionalMemberDto.isPresent()) {
			writerId = optionalMemberDto.get().getId();
		}

		commentService.writeComment(request, writerId, worldCupId, contentsId);
		return new RestApiResponse(1, "댓글 작성", null);
	}

	@Operation(
		summary = "새로운 액세스 토큰 반환",
		description = "리프레시 토큰을 받아 새로운 액세스 토큰을 반환합니다.",
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "액세스 토큰 생성"
			),
			@ApiResponse(
				responseCode = "400",
				description = "액세스 토큰 생성 실패",
				content = @Content(
					mediaType = "application/json",
					schema = @Schema(implementation = CustomErrorResponse.class)
				)
			),
		}
	)
	@PostMapping("/new-access-token")
	@ResponseStatus(OK)
	public RestApiResponse<CreateAccessTokenResponse> createAccessToken(
		@RequestBody CreateAccessTokenRequest createAccessTokenRequest
	) {

		String refreshToken = createAccessTokenRequest.refreshToken();
		String newAccessToken = null;
		int code = 1;
		String message = "엑세스 토큰 생성";

		try {

			newAccessToken = jwtService.createAccessTokenByRefreshToken(refreshToken);

		} catch (Exception ex) {

			tokenService.disableRefreshToken(refreshToken);

			refreshToken = null;
			code = 1010101;
			message = "다시 로그인해주세요.";

		}

		CreateAccessTokenResponse response = new CreateAccessTokenResponse(newAccessToken, refreshToken);
		return new RestApiResponse(code, message, response);
	}

	@Operation(
		summary = "미디어 파일 1건 반환",
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "미디어 파일 조회"
			),
		}
	)
	@GetMapping("/media-files/{mediaFileId}")
	@ResponseStatus(OK)
	public RestApiResponse<MediaFileResponse> getMediaFiles(
		@PathVariable Long mediaFileId,
		HttpServletResponse httpServletResponse
	) {

		httpServletResponse.setHeader("Cache-Control", "max-age=600");

		var response = mediaFileService.getMediaFile(mediaFileId);
		return new RestApiResponse(1, "미디어 파일 조회", response);

	}

	@Operation(
		summary = "미디어 파일 리스트 반환",
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "미디어 파일 조회"
			),
		}
	)
	@GetMapping("/media-files")
	@ResponseStatus(OK)
	public RestApiResponse<List<MediaFileResponse>> getMediaFiles(
		@RequestParam List<Long> mediaFileIds
	) {

		var response = mediaFileService.getMediaFile(mediaFileIds);
		return new RestApiResponse(1, "미디어 파일 조회", response);
	}

	@Operation(
		summary = "댓글 리스트 반환",
		parameters = {
			@Parameter(
				name = "worldCupId",
				description = "조회하고 싶은 댓글의 월드컵 식별자"
			),
			@Parameter(
				name = "offset",
				description = "댓글 리스트 offset"
			)
		},
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "댓글 리스트 조회"
			),
			@ApiResponse(
				responseCode = "404",
				description = "존재하지 않는 월드컵 게임",
				content = @Content(
					mediaType = "application/json",
					schema = @Schema(implementation = CustomErrorResponse.class)
				)
			),
		}
	)
	@ResponseStatus(OK)
	@GetMapping("/world-cups/{worldCupId}/comments")
	public RestApiResponse<List<GetCommentsListResponse>> getComments(
		@PathVariable Long worldCupId,
		@RequestParam Integer offset
	) {

		var response = commentService.getComments(worldCupId, offset);
		return new RestApiResponse(1, "코멘트 조회 성공", response);

	}

	@ResponseStatus(NO_CONTENT)
	@DeleteMapping("/comments/{commentId}")
	public RestApiResponse deleteComment(

		@PathVariable Long commentId,

		@Parameter(hidden = true)
		@CustomAuthentication(required = false)
		Optional<MemberDto> optionalMemberDto

	) {

		commentService.deleteComment(commentId, optionalMemberDto.get().getId());
		return new RestApiResponse(1, "코멘트 삭제 성공", null);

	}

}
