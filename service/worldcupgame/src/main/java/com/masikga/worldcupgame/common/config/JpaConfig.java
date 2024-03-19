package com.masikga.worldcupgame.common.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EntityScan({
        "com.masikga.member.model",
        "com.masikga.member.repository",
        "com.masikga.worldcupgame"})
@EnableJpaAuditing
@Configuration
public class JpaConfig {
}
