package com.example.demo.member.controller;

import com.example.demo.common.web.validation.NoSpace;
import com.example.demo.member.controller.dto.SignInRequest;
import com.example.demo.member.controller.dto.SignUpRequest;
import com.example.demo.common.error.CustomErrorResponse;
import com.example.demo.common.web.RestApiResponse;
import com.example.demo.member.service.MemberService;
import com.example.demo.member.service.dto.SignInResponse;
import com.example.demo.member.service.dto.VerifyDuplicatedServiceIdResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

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
                .message("검증 성공")
                .data(response)
                .build();
    }
}
