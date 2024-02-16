package com.masikga.itwc.domain.member.service;

import com.masikga.itwc.common.jwt.JwtService;
import com.masikga.itwc.domain.member.controller.request.SignInRequest;
import com.masikga.itwc.domain.member.controller.request.SignUpRequest;
import com.masikga.itwc.domain.member.controller.response.SignInResponse;
import com.masikga.itwc.domain.member.controller.response.VerifyDuplicatedNicknameResponse;
import com.masikga.itwc.domain.member.controller.response.VerifyDuplicatedServiceIdResponse;
import com.masikga.itwc.domain.member.exception.DuplicatedNicknameException;
import com.masikga.itwc.domain.member.exception.DuplicatedServiceIdException;
import com.masikga.itwc.domain.member.exception.NotFoundMemberException;
import com.masikga.itwc.domain.member.model.Member;
import com.masikga.itwc.domain.member.repository.MemberRepository;
import com.masikga.itwc.infra.rememberme.RememberMeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtService jwtService;
    private final RememberMeRepository rememberMeRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signUp(SignUpRequest request) {

        if (memberRepository.existsNicknameV2(request.nickname())) {
            throw new DuplicatedNicknameException();
        }
        if (memberRepository.existsServiceIdV2(request.serviceId())) {
            throw new DuplicatedServiceIdException();
        }

        Member newMember = Member.signUp(
                passwordEncoder,
                request.serviceId(),
                request.nickname(),
                request.password()
        );
        memberRepository.save(newMember);
    }

    @Transactional
    public SignInResponse signIn(SignInRequest request) {

        Member member = memberRepository
                .findByServiceId(request.serviceId())
                .orElseThrow(NotFoundMemberException::new);

        if (!passwordEncoder.matches(request.password(), member.getPassword())) {
            throw new NotFoundMemberException();
        }
        rememberMeRepository.save(member.getId());

        String refreshToken = jwtService.createRefreshTokenById(member.getId());
        String accessToken = jwtService.createAccessTokenById(member.getId());

        return SignInResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Transactional
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
