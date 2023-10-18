package com.example.demo.member.service;

import com.example.demo.common.jwt.JwtService;
import com.example.demo.member.controller.dto.SignInRequest;
import com.example.demo.member.controller.dto.SignUpRequest;
import com.example.demo.member.exception.DuplicatedNicknameException;
import com.example.demo.member.exception.DuplicatedServiceIdException;
import com.example.demo.member.exception.NotFoundMemberException;
import com.example.demo.member.model.Member;
import com.example.demo.member.model.MemberRepository;
import com.example.demo.member.service.dto.SignInResponse;
import com.example.demo.member.service.dto.VerifyDuplicatedServiceIdResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtService jwtService;

    @Transactional
    public void signUp(SignUpRequest request) {

        if(memberRepository.existsNickname(request.nickname())) {
            throw new DuplicatedNicknameException();
        }
        if(memberRepository.existsServiceId(request.serviceId())) {
            throw new DuplicatedServiceIdException();
        }

        Member newMember = Member.signUp(
                request.serviceId(),
                request.nickname(),
                request.password()
        );
        memberRepository.save(newMember);
    }

    public SignInResponse signIn(SignInRequest request) {
        Boolean existsMember = memberRepository
                .existsMemberWithServiceIdAndPassword(
                        request.serviceId(),
                        request.password()
                );

        if(!existsMember) {
            throw new NotFoundMemberException();
        }

        String refreshToken = jwtService.createRefreshToken(request.serviceId());
        String accessToken = jwtService.createAccessTokenByServiceId(request.serviceId());

        return SignInResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public VerifyDuplicatedServiceIdResponse existsServiceId(String serviceId) {
        Boolean isDuplicated = memberRepository.existsServiceId(serviceId);
        return new VerifyDuplicatedServiceIdResponse(isDuplicated);
    }
}
