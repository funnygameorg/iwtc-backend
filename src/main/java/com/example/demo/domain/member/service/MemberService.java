package com.example.demo.domain.member.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.common.jwt.JwtService;
import com.example.demo.domain.member.controller.request.SignInRequest;
import com.example.demo.domain.member.controller.request.SignUpRequest;
import com.example.demo.domain.member.controller.response.SignInResponse;
import com.example.demo.domain.member.controller.response.VerifyDuplicatedNicknameResponse;
import com.example.demo.domain.member.controller.response.VerifyDuplicatedServiceIdResponse;
import com.example.demo.domain.member.exception.DuplicatedNicknameException;
import com.example.demo.domain.member.exception.DuplicatedServiceIdException;
import com.example.demo.domain.member.exception.NotFoundMemberException;
import com.example.demo.domain.member.model.Member;
import com.example.demo.domain.member.repository.MemberRepository;
import com.example.demo.infra.rememberme.RememberMeRepository;

import lombok.RequiredArgsConstructor;

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

		if (memberRepository.existsNickname(request.nickname())) {
			throw new DuplicatedNicknameException();
		}
		if (memberRepository.existsServiceId(request.serviceId())) {
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
