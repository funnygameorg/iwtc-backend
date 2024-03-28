package com.masikga.member.service;

import com.masikga.jwt.common.config.JwtService;
import com.masikga.member.controller.request.SignInRequest;
import com.masikga.member.controller.request.SignUpRequest;
import com.masikga.member.controller.response.SignInResponse;
import com.masikga.member.controller.response.VerifyDuplicatedNicknameResponse;
import com.masikga.member.controller.response.VerifyDuplicatedServiceIdResponse;
import com.masikga.member.exception.DuplicatedNicknameExceptionMember;
import com.masikga.member.exception.DuplicatedServiceIdExceptionMember;
import com.masikga.member.exception.NotFoundMemberExceptionMember;
import com.masikga.member.infra.rememberme.RememberMeRepository;
import com.masikga.member.model.Member;
import com.masikga.member.repository.MemberRepository;
import com.masikga.model.member.GetMemberResponse;
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

    private final PasswordEncoder memberEncoder;

    @Transactional
    public void signUp(SignUpRequest request) {

        if (memberRepository.existsNicknameV2(request.nickname())) {
            throw new DuplicatedNicknameExceptionMember();
        }
        if (memberRepository.existsServiceIdV2(request.serviceId())) {
            throw new DuplicatedServiceIdExceptionMember();
        }

        Member newMember = Member.signUp(
                memberEncoder,
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
                .orElseThrow(NotFoundMemberExceptionMember::new);

        if (!memberEncoder.matches(request.password(), member.getPassword())) {
            throw new NotFoundMemberExceptionMember();
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

        Boolean isDuplicated = memberRepository.existsServiceIdV2(serviceId);

        return new VerifyDuplicatedServiceIdResponse(isDuplicated);

    }

    public VerifyDuplicatedNicknameResponse existsNickname(String nickname) {

        Boolean isDuplicatedNickname = memberRepository.existsNicknameV2(nickname);

        return new VerifyDuplicatedNicknameResponse(isDuplicatedNickname);

    }

    public GetMemberResponse findById(Long memberId) {

        Member member = memberRepository.findById(memberId).orElseThrow(NotFoundMemberExceptionMember::new);

        return new GetMemberResponse(member.getId(), member.getServiceId(), member.getNickname());

    }
}
