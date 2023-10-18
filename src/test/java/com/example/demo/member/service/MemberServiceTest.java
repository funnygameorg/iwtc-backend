package com.example.demo.member.service;

import com.example.demo.common.jwt.JwtService;
import com.example.demo.member.controller.dto.SignInRequest;
import com.example.demo.member.controller.dto.SignUpRequest;
import com.example.demo.member.service.dto.VerifyDuplicatedNicknameResponse;
import com.example.demo.member.service.dto.VerifyDuplicatedServiceIdResponse;
import com.example.demo.member.exception.DuplicatedNicknameException;
import com.example.demo.member.exception.DuplicatedServiceIdException;
import com.example.demo.member.exception.NotFoundMemberException;
import com.example.demo.member.model.MemberRepository;
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
    private MemberService sut;

    @Mock
    private MemberRepository memberRepository;

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
        sut.signUp(request);

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
                () -> sut.signUp(request)
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
                () -> sut.signUp(request)
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
        sut.signIn(request);

        // then
        then(memberRepository)
                .should(times(1))
                        .findByMemberIdByServiceIdAndPassword(any(), any());

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
        Optional<Long> memberId = Optional.ofNullable(null);

        // 사용자의 정보가 DB에 있다면
        given(memberRepository.findByMemberIdByServiceIdAndPassword(any(), any()))
                .willReturn(memberId);

        // when then
        assertThrows(
                NotFoundMemberException.class,
                () ->sut.signIn(request)
        );

        then(memberRepository)
                .should(times(1))
                .findByMemberIdByServiceIdAndPassword(any(), any());

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

        VerifyDuplicatedServiceIdResponse response = sut.existsServiceId(serviceId);

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

        VerifyDuplicatedServiceIdResponse response = sut.existsServiceId(serviceId);

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

        VerifyDuplicatedNicknameResponse response = sut.existsNickname(nickname);

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

        VerifyDuplicatedNicknameResponse response = sut.existsNickname(nickname);

        then(memberRepository)
                .should(times(1))
                .existsNickname(nickname);

        assert !response.isDuplicatedNickname();
    }
}
