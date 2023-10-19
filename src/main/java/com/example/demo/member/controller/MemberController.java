package com.example.demo.member.controller;

import com.example.demo.common.web.auth.RequireAuth;
import com.example.demo.common.web.memberresolver.MemberDto;
import com.example.demo.common.web.validation.NoSpace;
import com.example.demo.member.controller.dto.GetMySummaryResponse;
import com.example.demo.member.controller.dto.SignInRequest;
import com.example.demo.member.controller.dto.SignUpRequest;
import com.example.demo.common.error.CustomErrorResponse;
import com.example.demo.common.web.RestApiResponse;
import com.example.demo.member.service.MemberService;
import com.example.demo.member.service.dto.SignInResponse;
import com.example.demo.member.service.dto.VerifyDuplicatedNicknameResponse;
import com.example.demo.member.service.dto.VerifyDuplicatedServiceIdResponse;
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
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@Tag(
        name = "Member",
        description = "계정 관련 API"
)
@Validated
@RestController
@RequestMapping("/members")
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
        return RestApiResponse.builder()
                .code(1)
                .message("가입 성공")
                .data(null)
                .build();
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
                                    @Header(name="access-token", description = "엑세스 토큰"),
                                    @Header(name="refresh-token", description = "리프레시 토큰")}
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

        return RestApiResponse.builder()
                .code(1)
                .message("로그인 성공")
                .data(null)
                .build();
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
    public RestApiResponse verifyDuplicatedServiceId(
            @NoSpace
            @RequestParam
            @Size(min=6, max = 20, message = "사용자 아이디 : 6자리 이상, 20자리 이하")
            String serviceId
    ) {
        VerifyDuplicatedServiceIdResponse response = memberService.existsServiceId(serviceId);
        return RestApiResponse.builder()
                .code(1)
                .message("아이디 검증 성공")
                .data(response)
                .build();
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
    public RestApiResponse verifyDuplicatedNickname(
            @Schema(description = "사용자 닉네임")
            @Size(min = 2, max = 10, message = "사용자 닉네임 : 2자리 이상, 10자리 이하")
            @NoSpace
            String nickname
    ) {
        VerifyDuplicatedNicknameResponse response = memberService.existsNickname(nickname);
        return RestApiResponse.builder()
                .code(1)
                .message("닉네임 검증 성공")
                .data(response)
                .build();
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
    @RequireAuth
    @GetMapping("/me/summary")
    @ResponseStatus(OK)
    public RestApiResponse getMySummary(
            @Parameter(hidden = true) MemberDto memberDto
    ) {
        GetMySummaryResponse response = new GetMySummaryResponse(
                memberDto.id(),
                memberDto.serviceId(),
                memberDto.nickname()
        );

        return RestApiResponse.builder()
                .code(1)
                .message("정보 조회 성공")
                .data(response)
                .build();
    }
}
