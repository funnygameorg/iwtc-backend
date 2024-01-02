package com.example.demo.domain.etc.service;

import org.springframework.stereotype.Service;

import com.example.demo.common.jwt.JwtService;
import com.example.demo.common.web.auth.rememberme.RememberMeRepository;

import lombok.RequiredArgsConstructor;

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
