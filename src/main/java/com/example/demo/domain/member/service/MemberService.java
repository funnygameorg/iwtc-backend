package com.example.demo.domain.member.service;

import com.example.demo.common.jwt.JwtService;
import com.example.demo.common.web.auth.rememberme.RememberMeRepository;
import com.example.demo.domain.member.controller.request.SignInRequest;
import com.example.demo.domain.member.controller.request.SignUpRequest;
import com.example.demo.domain.member.exception.DuplicatedNicknameException;
import com.example.demo.domain.member.exception.DuplicatedServiceIdException;
import com.example.demo.domain.member.exception.NotFoundMemberException;
import com.example.demo.domain.member.model.Member;
import com.example.demo.domain.member.repository.MemberRepository;
import com.example.demo.domain.member.controller.response.SignInResponse;
import com.example.demo.domain.member.controller.response.VerifyDuplicatedNicknameResponse;
import com.example.demo.domain.member.controller.response.VerifyDuplicatedServiceIdResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtService jwtService;
    private final RememberMeRepository rememberMeRepository;
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
        Long memberId = memberRepository
                .findByMemberIdByServiceIdAndPassword(request.serviceId(), request.password())
                .orElseThrow(NotFoundMemberException::new);

        rememberMeRepository.save(memberId);

        String refreshToken = jwtService.createRefreshTokenById(memberId);
        String accessToken = jwtService.createAccessTokenById(memberId);

        return SignInResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public void signOut(String accessToken) {
        Long memberId = jwtService.getPayLoadByTokenIgnoreExpiredTime(accessToken);
        rememberMeRepository.signOut(accessToken, memberId);
    }

    public VerifyDuplicatedServiceIdResponse existsServiceId(String serviceId) {
        Boolean isDuplicated = memberRepository.existsServiceId(serviceId);
        return new VerifyDuplicatedServiceIdResponse(isDuplicated);
    }

    public VerifyDuplicatedNicknameResponse existsNickname(String nickname) {
        Boolean isDuplicatedNickname = memberRepository.existsNickname(nickname);
        return new VerifyDuplicatedNicknameResponse(isDuplicatedNickname);
    }
}
