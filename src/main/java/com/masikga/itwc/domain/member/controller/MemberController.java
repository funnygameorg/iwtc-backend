package com.masikga.itwc.domain.member.controller;

import static org.springframework.http.HttpStatus.*;

import java.util.Optional;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.masikga.itwc.common.error.CustomErrorResponse;
import com.masikga.itwc.common.web.CustomAuthentication;
import com.masikga.itwc.common.web.RestApiResponse;
import com.masikga.itwc.common.web.memberresolver.MemberDto;
import com.masikga.itwc.domain.member.controller.request.SignInRequest;
import com.masikga.itwc.domain.member.controller.request.SignUpRequest;
import com.masikga.itwc.domain.member.controller.response.GetMySummaryResponse;
import com.masikga.itwc.domain.member.controller.response.SignInResponse;
import com.masikga.itwc.domain.member.controller.response.VerifyDuplicatedNicknameResponse;
import com.masikga.itwc.domain.member.controller.response.VerifyDuplicatedServiceIdResponse;
import com.masikga.itwc.domain.member.controller.validator.VerifyMemberNickname;
import com.masikga.itwc.domain.member.controller.validator.VerifyMemberServiceId;
import com.masikga.itwc.domain.member.exception.NotFoundMemberException;
import com.masikga.itwc.domain.member.service.MemberService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(
	name = "Member",
	description = "계정 관련 API"
)
@Validated
@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;

	@Operation(
		summary = "일반 회원가입",
		description = "아무런 조건없이 가입이 가능합니다.",
		responses = {
			@ApiResponse(
				responseCode = "201",
				description = "가입 성공",
				content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponse.class))
			),
			@ApiResponse(
				responseCode = "400",
				description = "validation request",
				content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomErrorResponse.class))
			),
			@ApiResponse(
				responseCode = "409",
				description = "[아이디 중복, 닉네임 중복]",
				content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomErrorResponse.class))
			)
		}
	)
	@PostMapping("/sign-up")
	@ResponseStatus(CREATED)
	public RestApiResponse signUp(

		@Valid @RequestBody SignUpRequest request

	) {

		memberService.signUp(request);
		return new RestApiResponse(1, "가입 성공", null);
	}

	@Operation(
		summary = "로그인",
		description = "로그인을 하고 access, refresh token을 반환합니다.",
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "로그인 성공",
				content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponse.class)),
				headers = {
					@Header(name = "access-token", description = "엑세스 토큰"),
					@Header(name = "refresh-token", description = "리프레시 토큰")}
			),
			@ApiResponse(
				responseCode = "404",
				description = "사용자의 로그인 요청에 일치하는 멤버 정보가 없음",
				content = @Content(
					mediaType = "application/json", schema = @Schema(implementation = CustomErrorResponse.class))
			),
			@ApiResponse(
				responseCode = "400",
				description = "validation request",
				content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomErrorResponse.class))
			)
		}
	)
	@PostMapping("/sign-in")
	@ResponseStatus(OK)
	public RestApiResponse signIn(
		@Valid @RequestBody SignInRequest request,
		HttpServletResponse httpServletResponse
	) {
		SignInResponse response = memberService.signIn(request);

		httpServletResponse.addHeader("access-token", response.accessToken());
		httpServletResponse.addHeader("refresh-token", response.refreshToken());

		return new RestApiResponse(1, "로그인 성공", null);
	}

	@Operation(
		summary = "서비스 아이디 중복 확인",
		description = "서비스 아이디 중복 확인 합니다. true/false 반환",
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "중복 확인에 대한 결과를 true/false 반환",
				content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponse.class))
			),
			@ApiResponse(
				responseCode = "400",
				description = "validation request",
				content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomErrorResponse.class))
			)
		}
	)
	@GetMapping("/duplicated-check/service-id")
	@ResponseStatus(OK)
	public RestApiResponse<VerifyDuplicatedServiceIdResponse> verifyDuplicatedServiceId(
		@VerifyMemberServiceId
		@RequestParam
		String serviceId
	) {

		var response = memberService.existsServiceId(serviceId);
		return new RestApiResponse(1, "아이디 검증 성공", response);

	}

	@Operation(
		summary = "닉네임 중복 확인",
		description = "닉네임 중복 확인 합니다. true/false 반환",
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "중복 확인에 대한 결과를 true/false 반환",
				content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponse.class))
			),
			@ApiResponse(
				responseCode = "400",
				description = "validation request",
				content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomErrorResponse.class))
			)
		}
	)
	@GetMapping("/duplicated-check/nickname")
	@ResponseStatus(OK)
	public RestApiResponse<VerifyDuplicatedNicknameResponse> verifyDuplicatedNickname(

		@RequestParam
		@VerifyMemberNickname
		String nickname

	) {

		var response = memberService.existsNickname(nickname);
		return new RestApiResponse(1, "닉네임 검증 성공", response);

	}

	@Operation(
		summary = "token의 id를 참조해 자신의 정보를 반환",
		description = "간략화된 정보를 제공합니다. (access-token을 헤더에 넣어서 요청)",
		parameters = {
			@Parameter(
				in = ParameterIn.HEADER,
				name = "access-token",
				required = true,
				description = "Access Token",
				schema = @Schema(type = "string")
			)
		},
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "조회 성공",
				content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponse.class))
			),
			@ApiResponse(
				responseCode = "401",
				description = "인증에 관한 문제로 인한 실패",
				content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomErrorResponse.class))
			),
			@ApiResponse(
				responseCode = "404",
				description = "존재하지 않는 사용자의 요청",
				content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomErrorResponse.class))
			)
		}
	)
	@CustomAuthentication
	@GetMapping("/me/summary")
	@ResponseStatus(OK)
	public RestApiResponse getMySummary(

		@Parameter(hidden = true)
		@CustomAuthentication(required = false)
		Optional<MemberDto> optionalMemberDto,

		HttpServletResponse httpServletResponse
	) {

		MemberDto memberDto = optionalMemberDto.orElseThrow(NotFoundMemberException::new);

		httpServletResponse.setHeader("Cache-Control", "max-age=60");

		var response = new GetMySummaryResponse(memberDto);
		return new RestApiResponse(1, "정보 조회 성공", response);

	}

	@Operation(
		summary = "로그아웃",
		description = "사용자의 access token 일정 시간동안 무효, 스토리지의 remember 제거",
		parameters = {
			@Parameter(
				in = ParameterIn.HEADER,
				name = "access-token",
				required = true,
				description = "Access Token",
				schema = @Schema(type = "string")
			)
		},
		responses = {
			@ApiResponse(
				responseCode = "204",
				description = "로그아웃 성공",
				content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponse.class))
			)
		}
	)

	@GetMapping("/sign-out")
	@ResponseStatus(NO_CONTENT)
	public RestApiResponse signOut(

		@RequestHeader("access-token") String accessToken

	) {

		memberService.signOut(accessToken);
		return new RestApiResponse(1, "정보 조회 성공", null);

	}
}
