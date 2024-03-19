package com.masikga.member.domain.member.service;

import com.masikga.member.common.config.JwtService;
import com.masikga.member.controller.request.SignInRequest;
import com.masikga.member.controller.request.SignUpRequest;
import com.masikga.member.exception.DuplicatedNicknameExceptionMember;
import com.masikga.member.exception.DuplicatedServiceIdExceptionMember;
import com.masikga.member.exception.NotFoundMemberExceptionMember;
import com.masikga.member.helper.DataBaseCleanUp;
import com.masikga.member.helper.testbase.IntegrationBaseTest;
import com.masikga.member.model.Member;
import com.masikga.member.repository.MemberRepository;
import com.masikga.member.service.MemberService;
import com.masikga.worldcupgame.infra.rememberme.RememberMeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.masikga.member.helper.TestConstant.EXCEPTION_PREFIX;
import static com.masikga.member.helper.TestConstant.SUCCESS_PREFIX;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MemberServiceTest implements IntegrationBaseTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private RememberMeRepository rememberMeRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private DataBaseCleanUp dataBaseCleanUp;

    @AfterEach
    public void tearDown() {
        dataBaseCleanUp.truncateAllEntity();
    }

    @Nested
    @DisplayName("회원가입을 할 수 있다.")
    public class SignUp {

        @Test
        @DisplayName(SUCCESS_PREFIX)
        public void success1() {

            var serviceId = "Zero123";
            var nickname = "Hello123";
            var password = "!Awtewy1235*";

            var request = SignUpRequest.builder()
                    .serviceId(serviceId)
                    .nickname(nickname)
                    .password(password)
                    .build();

            //when
            memberService.signUp(request);

            var newMember = memberRepository.findByServiceId(serviceId).get();

            //then
            assertAll(
                    () -> assertThat(newMember.getServiceId()).isEqualTo("Zero123"),
                    () -> assertThat(newMember.getNickname()).isEqualTo("Hello123"),
                    () -> assertThat(passwordEncoder.matches(password, newMember.getPassword())).isTrue()
            );
        }

        @Test
        @DisplayName(EXCEPTION_PREFIX + "중복된 닉네임은 회원가입이 불가능하다.")
        public void fail1() {

            // given
            var serviceId = "Zero123";
            var nickname = "Hello123";
            var password = "!Awtewy1235*";

            var request = SignUpRequest.builder()
                    .serviceId(serviceId)
                    .nickname(nickname)
                    .password(password)
                    .build();

            var duplicateNicknameMember = Member.builder()
                    .serviceId("Five514")
                    .nickname("Hello123")
                    .password("!ABCDEFF123")
                    .build();

            memberRepository.save(duplicateNicknameMember);

            //when & then
            assertThrows(
                    DuplicatedNicknameExceptionMember.class,
                    () -> memberService.signUp(request)
            );
        }

        @Test
        @DisplayName(EXCEPTION_PREFIX + "중복된 서비스 아이디는 회원가입이 불가능하다.")
        public void fail2() {

            // given
            var serviceId = "Zero123";
            var nickname = "Hello123";
            var password = "!Awtewy1235*";

            var request = SignUpRequest.builder()
                    .serviceId(serviceId)
                    .nickname(nickname)
                    .password(password)
                    .build();

            var duplicateServiceIdMember = Member.builder()
                    .serviceId("Zero123")
                    .nickname("IPhone135")
                    .password("!ABCDEFF123")
                    .build();

            memberRepository.save(duplicateServiceIdMember);

            //when & then
            assertThrows(
                    DuplicatedServiceIdExceptionMember.class,
                    () -> memberService.signUp(request)
            );
        }

    }

    @Nested
    @DisplayName("로그인을 할 수 있다.")
    public class SignIn {

        @Test
        @DisplayName(SUCCESS_PREFIX)
        public void success1() {

            // given
            var signInRequest = SignInRequest.builder()
                    .serviceId("Hey123")
                    .password("magicPass12@@")
                    .build();

            var encodedPassword = passwordEncoder.encode("magicPass12@@");

            var newMember = Member.builder()
                    .serviceId("Hey123")
                    .nickname("Ja111az")
                    .password(encodedPassword)
                    .build();

            memberRepository.save(newMember);

            // when
            var signInResult = memberService.signIn(signInRequest);

            var accessToken = jwtService.createAccessTokenById(1L);
            var refreshToken = jwtService.createRefreshTokenById(1L);

            // then
            assertAll(
                    () -> assertThat(signInResult.accessToken()).isEqualTo(accessToken),
                    () -> assertThat(signInResult.refreshToken()).isEqualTo(refreshToken),
                    () -> assertThat(rememberMeRepository.isRemember(1L)).isTrue()
            );
        }

        @Test
        @DisplayName(EXCEPTION_PREFIX + " 존재하지 않는 사용자는 로그인 할 수 없다.")
        public void fail1() {

            // given
            var signInRequest = SignInRequest.builder()
                    .serviceId("Hey123")
                    .password("magicPass12@@")
                    .build();
            // when
            assertThrows(
                    NotFoundMemberExceptionMember.class,
                    () -> memberService.signIn(signInRequest)
            );
        }

    }

    @Nested
    @DisplayName("로그아웃을 할 수 있다.")
    public class SignOut {

        @Test
        @DisplayName(SUCCESS_PREFIX)
        public void success1() {

            // given
            var signInRequest = SignInRequest.builder()
                    .serviceId("Hey123")
                    .password("magicPass12@@")
                    .build();

            var encodedPassword = passwordEncoder.encode("magicPass12@@");

            var newMember = Member.builder()
                    .serviceId("Hey123")
                    .nickname("Ja111az")
                    .password(encodedPassword)
                    .build();

            memberRepository.save(newMember);
            memberService.signIn(signInRequest);
            var accessToken = jwtService.createAccessTokenById(1L);

            // when
            memberService.signOut(accessToken);

            // then
            assertThat(rememberMeRepository.isRemember(1L)).isFalse();

        }

    }

    @Nested
    @DisplayName("서비스 아이디 중복확인을 할 수 있다.")
    public class ExistsServiceId {

        @Test
        @DisplayName(SUCCESS_PREFIX)
        public void success1() {

            // given
            var newMember = Member.builder()
                    .serviceId("Hey123")
                    .nickname("Ja111az")
                    .password("ABC")
                    .build();

            memberRepository.save(newMember);

            // when
            var result = memberService.existsServiceId("Hey123");

            // then
            assertThat(result.isDuplicatedServiceId()).isTrue();

        }
    }

    @Nested
    @DisplayName("닉네임 중복확인을 할 수 있다.")
    public class ExistsName {

        @Test
        @DisplayName(SUCCESS_PREFIX)
        public void success1() {

            // given
            var newMember = Member.builder()
                    .serviceId("Hey123")
                    .nickname("Ja111az")
                    .password("ABC")
                    .build();

            memberRepository.save(newMember);

            // when
            var result = memberService.existsNickname("Ja111az");

            // then
            assertThat(result.isDuplicatedNickname()).isTrue();

        }

    }

}
