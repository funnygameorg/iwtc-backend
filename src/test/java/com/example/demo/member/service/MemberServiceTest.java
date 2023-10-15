package com.example.demo.member.service;

import com.example.demo.member.controller.dto.SignUpRequest;
import com.example.demo.member.exception.DuplicatedNicknameException;
import com.example.demo.member.exception.DuplicatedServiceIdException;
import com.example.demo.member.model.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @InjectMocks
    private MemberService sut;

    @Mock
    private MemberRepository memberRepository;

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
        sut.signUp(request, signUpDate);

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

        given(memberRepository.existsNickname(any())).willReturn(1);

        // when then
        assertThrows(
                DuplicatedNicknameException.class,
                () -> sut.signUp(request, signUpDate)
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

        given(memberRepository.existsServiceId(any())).willReturn(1);

        // when then
        assertThrows(
                DuplicatedServiceIdException.class,
                () -> sut.signUp(request, signUpDate)
        );

        then(memberRepository)
                .should(times(1))
                .existsServiceId(any());
    }
}
