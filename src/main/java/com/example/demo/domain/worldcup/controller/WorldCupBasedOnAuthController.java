package com.example.demo.domain.worldcup.controller;

import static org.springframework.http.HttpStatus.*;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.common.error.CustomErrorResponse;
import com.example.demo.common.web.RestApiResponse;
import com.example.demo.common.web.auth.CustomAuthentication;
import com.example.demo.common.web.memberresolver.MemberDto;
import com.example.demo.domain.worldcup.controller.request.CreateWorldCupContentsRequest;
import com.example.demo.domain.worldcup.controller.request.CreateWorldCupRequest;
import com.example.demo.domain.worldcup.controller.request.UpdateWorldCupContentsRequest;
import com.example.demo.domain.worldcup.controller.response.GetMyWorldCupContentsResponse;
import com.example.demo.domain.worldcup.controller.response.GetMyWorldCupResponse;
import com.example.demo.domain.worldcup.controller.response.GetWorldCupContentsResponse;
import com.example.demo.domain.worldcup.controller.response.GetWorldCupResponse;
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
	@ResponseStatus(OK)
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
	@ResponseStatus(OK)
	public RestApiResponse<GetWorldCupResponse> getWorldCup(
		@PathVariable long worldCupId,

		@Parameter(hidden = true)
		@CustomAuthentication
		Optional<MemberDto> memberDto
	) {
		return new RestApiResponse(
			1,
			"자신의 월드컵 조회",
			worldCupBasedOnAuthService.getMyWorldCup(worldCupId, memberDto.get().getId())
		);
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

	@Operation(
		summary = "월드컵 컨텐츠 1건 이상 생성",
		parameters = {
			@Parameter(
				name = "worldCupId",
				description = "생성하고 싶은 컨텐츠를 포함한 월드컵",
				required = true
			)
		},
		description = "월드컵 관리페이지에서 표시되는 월드컵 리스트 조회",
		security = @SecurityRequirement(name = "Authorization"),
		responses = {
			@ApiResponse(
				responseCode = "400",
				description = "월드컵 게임 작성자가 아님",
				content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomErrorResponse.class))
			),
			@ApiResponse(
				responseCode = "404",
				description = "존재하지 않는 월드컵",
				content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomErrorResponse.class))
			),

		}
	)
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

	@Operation(
		summary = "월드컵 컨텐츠 1건 수정",
		parameters = {
			@Parameter(
				name = "worldCupId",
				description = "수정하고 싶은 컨텐츠를 포함한 월드컵",
				required = true
			),
			@Parameter(
				name = "contentsId",
				description = "수정하고 싶은 컨텐츠 식별자",
				required = true
			)
		},
		description = "월드컵 관리페이지에서 표시되는 월드컵 리스트 조회",
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
	@PutMapping("/{worldCupId}/contents/{contentsId}")
	public RestApiResponse<Object> updateMyWorldCupContents(
		@Valid
		@RequestBody
		UpdateWorldCupContentsRequest request,

		@PathVariable
		long worldCupId,

		@PathVariable
		long contentsId,

		@Parameter(hidden = true)
		@CustomAuthentication
		Optional<MemberDto> memberDto
	) {

		var updateContentsId = worldCupBasedOnAuthService.updateMyWorldCupContents(
			request,
			worldCupId,
			contentsId,
			memberDto.get().getId()
		);

		return new RestApiResponse(
			1,
			"게임 생성",
			updateContentsId
		);

	}

	@Operation(
		summary = "월드컵 관리페이지에서 표시되는 월드컵 리스트 조회",
		parameters = {
			@Parameter(
				name = "worldCupId",
				description = "조회하고 싶은 컨텐츠의 월드컵 식별자",
				required = true
			),
			@Parameter(
				name = "contentsId",
				description = "조회하고 싶은 컨텐츠의 월드컵 컨텐츠 식별자",
				required = true
			)
		},
		description = "월드컵 관리페이지에서 표시되는 월드컵 리스트 조회",
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
	@DeleteMapping("/{worldCupId}/contents/{contentsId}")
	public RestApiResponse<Object> deleteMyWorldCupContents(

		@PathVariable
		long worldCupId,

		@PathVariable
		long contentsId,

		@Parameter(hidden = true)
		@CustomAuthentication
		Optional<MemberDto> memberDto
	) {

		var deletedContentsId = worldCupBasedOnAuthService.deleteMyWorldCupContents(
			worldCupId,
			contentsId,
			memberDto.get().getId()
		);

		return new RestApiResponse(
			1,
			"게임 삭제",
			deletedContentsId
		);

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

		return new RestApiResponse(
			1,
			"자신의 게임 리스트 조회",
			worldCupBasedOnAuthService.getMyWorldCupList(memberDto.get().getId())
		);

	}

	@Operation(
		summary = "월드컵 관리페이지에서 표시되는 컨텐츠 리스트 조회",
		description = "월드컵 관리페이지에서 표시되는 컨텐츠 리스트 조회",
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
				responseCode = "400",
				description = "월드컵 게임 작성자가 아님",
				content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomErrorResponse.class))
			),
			@ApiResponse(
				responseCode = "404",
				description = "존재하지 않는 월드컵",
				content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomErrorResponse.class))
			),
			@ApiResponse(
				responseCode = "409",
				description = "중복된 월드컵 타이틀을 사용하려고 함",
				content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomErrorResponse.class))
			)
		}
	)
	@ResponseStatus(OK)
	@GetMapping("/{worldCupId}/manage-contents")
	public RestApiResponse<List<GetMyWorldCupContentsResponse>> getMyWorldCupContentList(
		@PathVariable
		Long worldCupId,

		@Parameter(hidden = true)
		@CustomAuthentication
		Optional<MemberDto> memberDto
	) {

		return new RestApiResponse(
			1,
			"자신의 게임 컨텐츠 리스트 조회",
			worldCupBasedOnAuthService.getMyWorldCupContentsList(worldCupId, memberDto.get().getId())
		);

	}

}