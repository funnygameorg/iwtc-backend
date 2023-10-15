package com.example.demo.member.controller;

import com.example.demo.common.jwt.JwtService;
import com.example.demo.member.controller.dto.SignUpRequest;
import com.example.demo.member.service.MemberService;
import com.example.demo.member.service.MemberServiceTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = MemberController.class)
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private MemberService memberService;

    private static final String SIGN_UP_API = "/members/sign-up";

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
    public void 회원가입_실패_nickname(final String nickname) throws Exception {
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
    public void 회원가입_실패_password(final String password) throws Exception {
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
    public void 회원가입_실패_service(final String serviceId) throws Exception {
        // given
        SignUpRequest request = SignUpRequest.builder()
                .serviceId(serviceId)
                .nickname("주다라니")
                .password("password")
                .build();

        // when then
        mockMvc.perform(
                        post("/members/sign-up")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }
}