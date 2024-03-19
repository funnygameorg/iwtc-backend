package com.masikga.jwt.common.config;

import com.masikga.jwt.common.config.property.JwtProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class JwtConfig {

    private final JwtProperty jwtProperty;

    @Bean
    public JwtService jwtService() {
        return new JwtService(
                jwtProperty.getSecret(),
                jwtProperty.getTokenValidityMilliSeconds().getAccess(),
                jwtProperty.getTokenValidityMilliSeconds().getRefresh()
        );
    }
}