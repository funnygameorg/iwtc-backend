package com.example.demo.member.service;

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
import com.example.demo.domain.member.model.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

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

    @Test
    @DisplayName("회원가입 성공")
    public void 회원가입_성공() {
        LocalDateTime signUpDate = LocalDateTime.of(2022, 1, 1, 10, 10);
        String serviceId = "A";
        String nickname = "A";
        String password = "A";

        SignUpRequest request = SignUpRequest.builder()
                .serviceId(serviceId)
                .nickname(nickname)
                .password(password)
                .build();

        // When
        memberServiceTest.signUp(request);

        then(memberRepository)
                .should(times(1))
                .save(any());
    }

    @Test
    @DisplayName("회원가입 실패 - 닉네임 중복")
    public void 회원가입_실패_닉네임_중복() {
        LocalDateTime signUpDate = LocalDateTime.of(2022, 1, 1, 10, 10);
        String serviceId = "A";
        String nickname = "A";
        String password = "A";

        SignUpRequest request = SignUpRequest.builder()
                .serviceId(serviceId)
                .nickname(nickname)
                .password(password)
                .build();

        given(memberRepository.existsNickname(any())).willReturn(true);

        // when then
        assertThrows(
                DuplicatedNicknameException.class,
                () -> memberServiceTest.signUp(request)
        );

        then(memberRepository)
                .should(times(1))
                .existsNickname(any());
    }

    @Test
    @DisplayName("회원가입 실패 - 아이디 중복")
    public void 회원가입_실패_아이디_중복() {
        LocalDateTime signUpDate = LocalDateTime.of(2022, 1, 1, 10, 10);
        String serviceId = "A";
        String nickname = "A";
        String password = "A";

        SignUpRequest request = SignUpRequest.builder()
                .serviceId(serviceId)
                .nickname(nickname)
                .password(password)
                .build();

        given(memberRepository.existsServiceId(any())).willReturn(true);

        // when then
        assertThrows(
                DuplicatedServiceIdException.class,
                () -> memberServiceTest.signUp(request)
        );

        then(memberRepository)
                .should(times(1))
                .existsServiceId(any());
    }

    @Test
    @DisplayName("로그인 성공")
    public void 로그인_성공() {
        // given
        SignInRequest request = SignInRequest.builder()
                .serviceId("LoginRequesterId")
                .password("tempPassword")
                .build();
        Optional<Long> memberId = Optional.of(1L);
        // 사용자의 정보가 DB에 있다면
        given(memberRepository.findByMemberIdByServiceIdAndPassword(any(), any()))
                .willReturn(memberId);

        // when
        memberServiceTest.signIn(request);

        // then
        then(memberRepository)
                .should(times(1))
                        .findByMemberIdByServiceIdAndPassword(any(), any());

        then(rememberMeRepository)
                .should(times(1))
                        .save(memberId.get());

        then(jwtService)
                .should(times(1))
                .createAccessTokenById(memberId.get());

        then(jwtService)
                .should(times(1))
                .createRefreshTokenById(memberId.get());
    }

    @Test
    @DisplayName("로그인 실패 - 없는 아이디")
    public void 로그인_실패_없는_아이디() {
        // given
        SignInRequest request = SignInRequest.builder()
                .serviceId("LoginRequesterId")
                .password("tempPassword")
                .build();
        Optional<Long> optionalMemberId = Optional.empty();

        // 사용자의 정보가 DB에 있다면
        given(memberRepository.findByMemberIdByServiceIdAndPassword(any(), any()))
                .willReturn(optionalMemberId);

        // when then
        assertThrows(
                NotFoundMemberException.class,
                () ->memberServiceTest.signIn(request)
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
    
    @Test
    @DisplayName("아이디 중복 체크 - 중복된 아이디가 없다.")
    public void 제공한_아이디가_중복인가_X() {
        String serviceId = "testServiceID";
        given(memberRepository.existsServiceId(serviceId))
                .willReturn(false);

        VerifyDuplicatedServiceIdResponse response = memberServiceTest.existsServiceId(serviceId);

        then(memberRepository)
                .should(times(1))
                .existsServiceId(serviceId);

        assert !response.isDuplicatedServiceId();
    }

    @Test
    @DisplayName("아이디 중복 체크 - 중복된 아이디가 있다.")
    public void 제공한_아이디가_중복인가_O() {
        String serviceId = "testServiceID";
        given(memberRepository.existsServiceId(serviceId))
                .willReturn(true);

        VerifyDuplicatedServiceIdResponse response = memberServiceTest.existsServiceId(serviceId);

        then(memberRepository)
                .should(times(1))
                .existsServiceId(serviceId);

        assert response.isDuplicatedServiceId();
    }

    @Test
    @DisplayName("닉네임 중복 체크 - 중복된 닉네임이 있다.")
    public void 제공한_닉네임이_중복인가_O() {
        String nickname = "testNickname";

        given(memberRepository.existsNickname(nickname))
                .willReturn(true);

        VerifyDuplicatedNicknameResponse response = memberServiceTest.existsNickname(nickname);

        then(memberRepository)
                .should(times(1))
                .existsNickname(nickname);

        assert response.isDuplicatedNickname();
    }

    @Test
    @DisplayName("닉네임 중복 체크 - 중복된 닉네임이 없다.")
    public void 제공한_닉네임이_중복인가_X() {
        String nickname = "testNickname";

        given(memberRepository.existsNickname(nickname))
                .willReturn(false);

        VerifyDuplicatedNicknameResponse response = memberServiceTest.existsNickname(nickname);

        then(memberRepository)
                .should(times(1))
                .existsNickname(nickname);

        assert !response.isDuplicatedNickname();
    }

    @Test
    @DisplayName("로그아웃 - 성공")
    public void 로그아웃_성공() {

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
