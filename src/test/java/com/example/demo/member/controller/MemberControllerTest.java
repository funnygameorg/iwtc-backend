package com.example.demo.member.controller;

import com.example.demo.helper.config.TestWebConfig;
import com.example.demo.common.config.WebConfig;
import com.example.demo.domain.member.controller.MemberController;
import com.example.demo.domain.member.controller.request.SignInRequest;
import com.example.demo.domain.member.controller.request.SignUpRequest;
import com.example.demo.domain.member.service.MemberService;
import com.example.demo.domain.member.controller.response.SignInResponse;
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
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        value = MemberController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebConfig.class)
        })
@Import(TestWebConfig.class)
class MemberControllerTest {


    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @MockBean private MemberService memberService;

    private final String ROOT_PATH = "/api";

    private final String GET_ME_SUMMARY_API = ROOT_PATH + "/members/me/summary";
    private final String SIGN_UP_API = ROOT_PATH + "/members/sign-up";
    private final String SIGN_OUT_API = ROOT_PATH + "/members/sign-out";
    private final String SIGN_IN_API = ROOT_PATH + "/members/sign-in";
    private final String VERIFY_DUPLICATED_ID_API = ROOT_PATH + "/members/duplicated-check/service-id";
    private final String VERIFY_DUPLICATED_NICKNAME_API = ROOT_PATH + "/members/duplicated-check/nickname";


    @Test
    @DisplayName("자신의 정보 조회")
    public void getMeSummary() throws Exception {

        mockMvc.perform(get(GET_ME_SUMMARY_API))
                .andExpect(status().isOk());
    }

    @DisplayName("회원가입")
    @Test
    public void SIGN_UP_API_1() throws Exception {
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
    @DisplayName("회원가입 - nickname 검증 [2자리 미만, 10자리 초과 불가] (예외)")
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
    public void SIGN_UP_API_2(String nickname) throws Exception {

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
    @DisplayName("회원가입 - password 검증 [6자리 미만 불가](예외)")
    public void SIGN_UP_API_3(String password) throws Exception {
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
    @DisplayName("회원가입 - serviceId 검증 [2자리 미만, 10자리 초과 불가] (예외)")
    public void SIGN_UP_API_4(String serviceId) throws Exception {
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
    @DisplayName("로그인")
    public void SIGN_IN_API_1() throws Exception {
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
                post(SIGN_IN_API)
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
    @DisplayName("로그인 - serviceId 검증 [6자리 미만, 20자리 초과 불가](예외)")
    public void SIGN_IN_API_2(String serviceId) throws Exception{
        SignInRequest request = SignInRequest.builder()
                .serviceId(serviceId)
                .password("AAAA@32fSD")
                .build();
        mockMvc.perform(
                        post(SIGN_IN_API)
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
    @DisplayName("로그인 - password 검증 [5자리 이하 불가] (예외)")
    public void SIGN_IN_API_3(String password) throws Exception{
        SignInRequest request = SignInRequest.builder()
                .serviceId("AAAA@32fSD")
                .password(password)
                .build();
        // when then
        mockMvc.perform(
                        post(SIGN_IN_API)
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("로그아웃")
    public void SIGN_OUT_API_1() throws Exception{
        mockMvc.perform(
                get(SIGN_OUT_API)
                        .header("access-token", "TestAccessTokenValue")
        )
                .andExpect(status().isNoContent());
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
    @DisplayName("serviceId 중복 검사 - serviceId 검증")
    public void VERIFY_DUPLICATED_ID_API_1(String serviceId) throws Exception{
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
    @DisplayName("serviceId 중복 검사 - serviceId 검증[6자리 미만, 20자리 초과 불가] (예외)")
    public void VERIFY_DUPLICATED_ID_API_2(String serviceId) throws Exception{
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
    @DisplayName("nickname 중복 검사 - nickname 검증")
    public void VERIFY_DUPLICATED_NICKNAME_API_1(String nickname) throws Exception{
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
    @DisplayName("nickname 중복 검사 - nickname 검증 [2자리 미만, 10자리 초과 불가] (예외)")
    public void VERIFY_DUPLICATED_NICKNAME_API_2(String nickname) throws Exception{
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