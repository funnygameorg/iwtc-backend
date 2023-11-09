package com.example.demo.member.service;

import com.example.demo.TestConstant;
import com.example.demo.common.jwt.JwtService;
import com.example.demo.common.web.auth.rememberme.RememberMeRepository;
import com.example.demo.domain.member.controller.request.SignInRequest;
import com.example.demo.domain.member.controller.request.SignUpRequest;
import com.example.demo.domain.member.service.MemberService;
import com.example.demo.domain.member.controller.response.VerifyDuplicatedNicknameResponse;
import com.example.demo.domain.member.controller.response.VerifyDuplicatedServiceIdResponse;
import com.example.demo.domain.member.exception.DuplicatedNicknameException;
import com.example.demo.domain.member.exception.DuplicatedServiceIdException;
import com.example.demo.domain.member.exception.NotFoundMemberException;
import com.example.demo.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testcontainers.shaded.org.bouncycastle.pqc.crypto.newhope.NHOtherInfoGenerator;

import java.security.PublicKey;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.example.demo.TestConstant.EXCEPTION_PREFIX;
import static com.example.demo.TestConstant.SUCCESS_PREFIX;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @InjectMocks
    private MemberService memberServiceTest;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private RememberMeRepository rememberMeRepository;
    @Mock
    private JwtService jwtService;

    @Nested
    @DisplayName("회원가입을 할 수 있다.")
    class signUp {
        final private SignUpRequest REQUEST = SignUpRequest.builder()
                .serviceId("A")
                .nickname("A")
                .password("A")
                .build();

        @Test
        @DisplayName(SUCCESS_PREFIX)
        public void success() {
            // When
            memberServiceTest.signUp(REQUEST);

            then(memberRepository)
                    .should(times(1))
                    .save(any());
        }

        @Test
        @DisplayName(EXCEPTION_PREFIX + "닉네임 중복")
        public void fail() {
            given(memberRepository.existsNickname(any())).willReturn(true);

            // when then
            assertThrows(
                    DuplicatedNicknameException.class,
                    () -> memberServiceTest.signUp(REQUEST)
            );

            then(memberRepository)
                    .should(times(1))
                    .existsNickname(any());
        }

        @Test
        @DisplayName(EXCEPTION_PREFIX + "아이디 중복")
        public void fail2() {
            given(memberRepository.existsServiceId(any())).willReturn(true);

            // when then
            assertThrows(
                    DuplicatedServiceIdException.class,
                    () -> memberServiceTest.signUp(REQUEST)
            );

            then(memberRepository)
                    .should(times(1))
                    .existsServiceId(any());
        }
    }



    @Nested
    @DisplayName("로그인을 할 수 있다.")
    public class signIn {
        private Optional<Long> optionalMemberId;
        final private SignInRequest REQUEST = SignInRequest.builder()
                .serviceId("LoginRequesterId")
                .password("tempPassword")
                .build();

        @Test
        @DisplayName(SUCCESS_PREFIX)
        public void success() {
            // given
            optionalMemberId = Optional.of(1L);

            // 사용자의 정보가 DB에 있다면
            given(memberRepository.findByMemberIdByServiceIdAndPassword(any(), any()))
                    .willReturn(optionalMemberId);

            // when
            memberServiceTest.signIn(REQUEST);

            // then
            then(memberRepository)
                    .should(times(1))
                    .findByMemberIdByServiceIdAndPassword(any(), any());

            then(rememberMeRepository)
                    .should(times(1))
                    .save(optionalMemberId.get());

            then(jwtService)
                    .should(times(1))
                    .createAccessTokenById(optionalMemberId.get());

            then(jwtService)
                    .should(times(1))
                    .createRefreshTokenById(optionalMemberId.get());
        }


        @Test
        @DisplayName(EXCEPTION_PREFIX + "존재하지 않는 아이디")
        public void fail() {
            // given
            optionalMemberId = Optional.empty();

            // 사용자의 정보가 DB에 있다면
            given(memberRepository.findByMemberIdByServiceIdAndPassword(any(), any()))
                    .willReturn(optionalMemberId);

            // when then
            assertThrows(
                    NotFoundMemberException.class,
                    () ->memberServiceTest.signIn(REQUEST)
            );

            then(memberRepository)
                    .should(times(1))
                    .findByMemberIdByServiceIdAndPassword(any(), any());

            then(rememberMeRepository)
                    .should(never())
                    .save(any());

            then(jwtService)
                    .should(never())
                    .createAccessTokenById(any());

            then(jwtService)
                    .should(never())
                    .createRefreshTokenById(any());
        }

    }



    @Nested
    @DisplayName("아이디 중복 체크를 할 수 있다.")
    class existsServiceId {
        final private String SERVICE_ID = "testServiceID";

        @Test
        @DisplayName(SUCCESS_PREFIX + "중복된 아이디 O")
        public void isFalse() {
            given(memberRepository.existsServiceId(SERVICE_ID))
                    .willReturn(false);

            VerifyDuplicatedServiceIdResponse response = memberServiceTest.existsServiceId(SERVICE_ID);

            then(memberRepository)
                    .should(times(1))
                    .existsServiceId(SERVICE_ID);

            assert !response.isDuplicatedServiceId();
        }

        @Test
        @DisplayName(EXCEPTION_PREFIX + "중복된 아이디 X")
        public void isTrue() {
            given(memberRepository.existsServiceId(SERVICE_ID))
                    .willReturn(true);

            VerifyDuplicatedServiceIdResponse response = memberServiceTest.existsServiceId(SERVICE_ID);

            then(memberRepository)
                    .should(times(1))
                    .existsServiceId(SERVICE_ID);

            assert response.isDuplicatedServiceId();
        }
    }



    @Nested
    @DisplayName("닉네임 중복 체크를 할수 있다.")
    class existsNickname {

        final private String NICKNAME = "testNickname";

        @Test
        @DisplayName(SUCCESS_PREFIX + "중복된 닉네임 O")
        public void isTrue() {

            given(memberRepository.existsNickname(NICKNAME))
                    .willReturn(true);

            VerifyDuplicatedNicknameResponse response = memberServiceTest.existsNickname(NICKNAME);

            then(memberRepository)
                    .should(times(1))
                    .existsNickname(NICKNAME);

            assert response.isDuplicatedNickname();
        }

        @Test
        @DisplayName(EXCEPTION_PREFIX + "중복된 닉네임 X")
        public void isFalse() {

            given(memberRepository.existsNickname(NICKNAME))
                    .willReturn(false);

            VerifyDuplicatedNicknameResponse response = memberServiceTest.existsNickname(NICKNAME);

            then(memberRepository)
                    .should(times(1))
                    .existsNickname(NICKNAME);

            assert !response.isDuplicatedNickname();
        }

    }



    @Test
    @DisplayName(SUCCESS_PREFIX + "로그아웃을 할 수 있다.")
    public void signOut() {

        String accessToken = "testAccessToken";
        Long memberId = 1L;

        given(jwtService.getPayLoadByTokenIgnoreExpiredTime(accessToken))
                .willReturn(memberId);

        memberServiceTest.signOut(accessToken);

        then(rememberMeRepository)
                .should(times(1))
                .signOut(accessToken, memberId);
    }
}
