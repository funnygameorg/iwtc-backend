package com.masikga.worldcupgame.domain.etc.service;

import com.masikga.jwt.common.config.JwtService;
import com.masikga.rememberme.RememberMeRepository;
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
