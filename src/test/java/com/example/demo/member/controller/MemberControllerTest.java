package com.example.demo.member.controller;

import com.example.demo.common.config.WebConfig;
import com.example.demo.common.jwt.JwtService;
import com.example.demo.common.web.auth.rememberme.RememberMeRepository;
import com.example.demo.member.controller.dto.SignInRequest;
import com.example.demo.member.controller.dto.SignUpRequest;
import com.example.demo.member.model.MemberRepository;
import com.example.demo.member.service.MemberService;
import com.example.demo.member.service.dto.SignInResponse;
import com.example.demo.helper.web.TestWebConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        value = MemberController.class,
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE, classes = WebConfig.class
                )
        },
        includeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE, classes = TestWebConfig.class
                )
        }
)
class MemberControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @MockBean private JwtService jwtService;
    @MockBean private MemberRepository memberRepository;
    @MockBean private MemberService memberService;
    @MockBean private RememberMeRepository rememberMeRepository;


    private final String ROOT_PATH = "/api";

    private final String GET_ME_SUMMARY_API = ROOT_PATH + "/members/me/summary";
    private final String SIGN_UP_API = ROOT_PATH + "/members/sign-up";
    private final String LOGIN_API = ROOT_PATH + "/members/sign-in";
    private final String VERIFY_DUPLICATED_ID_API = ROOT_PATH + "/members/duplicated-check/service-id";
    private final String VERIFY_DUPLICATED_NICKNAME_API = ROOT_PATH + "/members/duplicated-check/nickname";


    @Test
    @DisplayName("자신의 정보 조회")
    public void getMeSummary() throws Exception {
        mockMvc.perform(get(GET_ME_SUMMARY_API))
                .andExpect(status().isOk());
    }

    @DisplayName("회원가입 성공")
    @Test
    public void 회원가입_성공() throws Exception {
        // given
        SignUpRequest request = SignUpRequest.builder()
                .serviceId("itwc123")
                .nickname("주다")
                .password("12341234")
                .build();

        // when then
        mockMvc.perform(
                post(SIGN_UP_API)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON)
        )
                .andExpect(status().isCreated());
    }

    @ParameterizedTest
    @DisplayName("회원가입 요청 살패 - nickname 검증")
    @CsvSource(value = {
            "주",
            "트레비주리오12345",
            "A",
            "1",
            "a ",
            "a a a a a a a a a a a",
            "aaaaa aaaaa",
            "a a a a a"}
    )
    @NullAndEmptySource
    public void 회원가입_실패_nickname(String nickname) throws Exception {
        // given
        SignUpRequest request = SignUpRequest.builder()
                .serviceId("itwc123")
                .nickname(nickname)
                .password("12341234")
                .build();

        // when then
        mockMvc.perform(
                post(SIGN_UP_API)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON)
        )
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @CsvSource(value = {
            "AAAAA",
            " A A B A B A ",
            "123",
            "Θ\nΘ\n"}
    )
    @NullAndEmptySource
    @DisplayName("회원가입 요청 실패 - password 검증")
    public void 회원가입_실패_password(String password) throws Exception {
        // given
        SignUpRequest request = SignUpRequest.builder()
                .serviceId("itwc123")
                .nickname("주다라니")
                .password(password)
                .build();

        // when then
        mockMvc.perform(
                        post(SIGN_UP_API)
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }
    @ParameterizedTest
    @CsvSource(value = {
            "AAAAA",
            " A A B A B A ",
            "123",
            "Θ\nΘ\n"}
    )
    @NullAndEmptySource
    @DisplayName("회원가입 요청 실패 - serviceId 검증")
    public void 회원가입_실패_service(String serviceId) throws Exception {
        // given
        SignUpRequest request = SignUpRequest.builder()
                .serviceId(serviceId)
                .nickname("주다라니")
                .password("password")
                .build();

        // when then
        mockMvc.perform(
                        post(SIGN_UP_API)
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("로그인 요청 성공")
    public void 로그인요청_성공() throws Exception {
        // given
        SignInRequest request = SignInRequest.builder()
                .serviceId("AAAAVEf23")
                .password("AAAA@32fSD")
                .build();
        SignInResponse response = SignInResponse.builder()
                .accessToken("A")
                .refreshToken("A")
                .build();
        given(memberService.signIn(request))
                .willReturn(response);

        // when then
        mockMvc.perform(
                post(LOGIN_API)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(header().exists("access-token"))
                .andExpect(header().exists("refresh-token"));
    }

    @ParameterizedTest
    @CsvSource(value = {
            "AAAAA",
            " A A B A B A ",
            "123",
            "Θ\nΘ\n"}
    )
    @NullAndEmptySource
    @DisplayName("로그인 실패 - serviceId 검증")
    public void 로그인요청_실패_service(String serviceId) throws Exception{
        SignInRequest request = SignInRequest.builder()
                .serviceId(serviceId)
                .password("AAAA@32fSD")
                .build();

        mockMvc.perform(
                        post(LOGIN_API)
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
        // when then
    }

    @ParameterizedTest
    @CsvSource(value = {
            "AAAAA",
            "AABAB",
            "12345",
            "123",
            "Θ\nΘ\n"}
    )
    @DisplayName("로그인 실패 - password 검증")
    public void 로그인요청_실패_password(String password) throws Exception{
        SignInRequest request = SignInRequest.builder()
                .serviceId("AAAA@32fSD")
                .password(password)
                .build();
        // when then
        mockMvc.perform(
                        post(LOGIN_API)
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @CsvSource(value = {
            "AAAAAAF",
            "AABAB124",
            "12345aZ",
            "123fffFfAA",
            "aaaaaaa",
            "1231231231"}
    )
    @DisplayName("serviceId 중복 검사 성공 - serviceId 검증")
    public void 서비스_아이디_중복_검사_성공(String serviceId) throws Exception{
        MultiValueMap<String, String> param = new LinkedMultiValueMap();
        param.add("serviceId", serviceId);

        // when then
        mockMvc.perform(
                        get(VERIFY_DUPLICATED_ID_API)
                                .params(param)
                )
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @CsvSource(value = {
            "AAAAA",
            "AAAAAAAAAAAAAAAAAAAA1",
            " A A B A B A ",
            "123",
            "Θ\nΘ\n"}
    )
    @NullAndEmptySource
    @DisplayName("serviceId 중복 검사 실패 - serviceId 검증")
    public void 서비스_아이디_중복_검사_실패(String serviceId) throws Exception{
        MultiValueMap<String, String> param = new LinkedMultiValueMap();
        param.add("serviceId", serviceId);

        // when then
        mockMvc.perform(
                        get(VERIFY_DUPLICATED_ID_API)
                                .params(param)
                )
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @CsvSource(value = {
            "AAAAA",
            "AAAAAA",
            "123456",
            "12"}
    )
    @DisplayName("nickname 중복 검사 성공 - nickname 검증")
    public void 닉네임_중복_검사_성공(String nickname) throws Exception{
        MultiValueMap<String, String> param = new LinkedMultiValueMap();
        param.add("nickname", nickname);

        // when then
        mockMvc.perform(
                        get(VERIFY_DUPLICATED_NICKNAME_API)
                                .params(param)
                )
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @CsvSource(value = {
            "A",
            "AAAAAAAAAAA",
            "12345678901",
            "1 2",
    }
    )
    @NullAndEmptySource
    @DisplayName("nickname 중복 검사 실패 - nickname 검증")
    public void 닉네임_중복_검사_실패(String nickname) throws Exception{
        MultiValueMap<String, String> param = new LinkedMultiValueMap();
        param.add("nickname", nickname);

        // when then
        mockMvc.perform(
                        get(VERIFY_DUPLICATED_NICKNAME_API)
                                .params(param)
                )
                .andExpect(status().isBadRequest());
    }
}