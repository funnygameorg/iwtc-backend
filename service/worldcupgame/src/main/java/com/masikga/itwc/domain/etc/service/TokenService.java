package com.masikga.itwc.domain.etc.service;

import com.masikga.itwc.common.jwt.JwtService;
import com.masikga.itwc.infra.rememberme.RememberMeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {

	private final RememberMeRepository rememberMeRepository;
	private final JwtService jwtService;

	public void disableRefreshToken(String refreshToken) {

		Long memberId = jwtService.getPayLoadByTokenIgnoreExpiredTime(refreshToken);

		rememberMeRepository.removeRemember(memberId);

	}

}
