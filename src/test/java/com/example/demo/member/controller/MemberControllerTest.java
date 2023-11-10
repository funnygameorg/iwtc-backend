package com.example.demo.member.controller;

import com.example.demo.helper.config.TestWebConfig;
import com.example.demo.common.config.WebConfig;
import com.example.demo.domain.member.controller.MemberController;
import com.example.demo.domain.member.controller.request.SignInRequest;
import com.example.demo.domain.member.controller.request.SignUpRequest;
import com.example.demo.domain.member.service.MemberService;
import com.example.demo.domain.member.controller.response.SignInResponse;
import com.example.demo.helper.testbase.WebMvcBaseTest;
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

import static com.example.demo.helper.TestConstant.EXCEPTION_PREFIX;
import static com.example.demo.helper.TestConstant.SUCCESS_PREFIX;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class MemberControllerTest extends WebMvcBaseTest {
    private final String GET_ME_SUMMARY_API = MEMBER_PATH + "/me/summary";
    private final String SIGN_UP_API = MEMBER_PATH + "/sign-up";
    private final String SIGN_OUT_API = MEMBER_PATH + "/sign-out";
    private final String SIGN_IN_API = MEMBER_PATH + "/sign-in";
    private final String VERIFY_DUPLICATED_ID_API = MEMBER_PATH + "/duplicated-check/service-id";
    private final String VERIFY_DUPLICATED_NICKNAME_API = MEMBER_PATH + "/duplicated-check/nickname";


    @Test
    @DisplayName(SUCCESS_PREFIX + "자신의 정보 조회 요청 검증")
    public void getMeSummary() throws Exception {

        mockMvc.perform(get(GET_ME_SUMMARY_API))
                .andExpect(status().isOk());
    }

    @Nested
    @DisplayName("회웝가입 요청 검증")
    public class signUpApi {
        @DisplayName(SUCCESS_PREFIX)
        @Test
        public void success1() throws Exception {
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
        @DisplayName(EXCEPTION_PREFIX + "nickname : 2자리 미만, 10자리 초과 불가")
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
        public void fail1(String nickname) throws Exception {

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
        @DisplayName(EXCEPTION_PREFIX + "password : 6자리 미만 불가")
        public void fail2(String password) throws Exception {
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
        @DisplayName(EXCEPTION_PREFIX + "serviceId : 2자리 미만, 10자리 초과 불가")
        public void fail3(String serviceId) throws Exception {
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

    }

    @Nested
    @DisplayName("로그인 요청 검증")
    public class signIn {

        @Test
        @DisplayName(SUCCESS_PREFIX)
        public void success1() throws Exception {
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
        @DisplayName(EXCEPTION_PREFIX + "serviceId : 6자리 미만, 20자리 초과 불가")
        public void fail1(String serviceId) throws Exception{
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
        @DisplayName(EXCEPTION_PREFIX + "password : 5자리 이하 불가")
        public void fail2(String password) throws Exception{
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

    }

    @Test
    @DisplayName(SUCCESS_PREFIX + "로그아웃")
    public void signOut() throws Exception{
        mockMvc.perform(
                get(SIGN_OUT_API)
                        .header("access-token", "TestAccessTokenValue")
        )
                .andExpect(status().isNoContent());
    }

    @Nested
    @DisplayName("serviceId 중복 검사 요청 검증")
    public class verifyDuplicatedServiceId {
        @ParameterizedTest
        @CsvSource(value = {
                "AAAAAAF",
                "AABAB124",
                "12345aZ",
                "123fffFfAA",
                "aaaaaaa",
                "1231231231"}
        )
        @DisplayName(SUCCESS_PREFIX)
        public void success1(String serviceId) throws Exception{
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
        @DisplayName(EXCEPTION_PREFIX + "serviceId : 6자리 미만, 20자리 초과 불가")
        public void fail1(String serviceId) throws Exception{
            MultiValueMap<String, String> param = new LinkedMultiValueMap();
            param.add("serviceId", serviceId);

            // when then
            mockMvc.perform(
                            get(VERIFY_DUPLICATED_ID_API)
                                    .params(param)
                    )
                    .andExpect(status().isBadRequest());
        }

    }

    @Nested
    @DisplayName("nickname 중복 검사 요청 검증")
    public class verifyDuplicatedNickname {

        @ParameterizedTest
        @CsvSource(value = {
                "AAAAA",
                "AAAAAA",
                "123456",
                "12"}
        )
        @DisplayName(SUCCESS_PREFIX)
        public void success(String nickname) throws Exception{
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
        @DisplayName(EXCEPTION_PREFIX + "nickname : 2자리 미만, 10자리 초과 불가")
        public void fail1(String nickname) throws Exception{
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
}