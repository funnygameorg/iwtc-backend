package com.masikga.worldcupgame;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

// JPA Repository Scan, Jpa Scan 범위 조정하기
@EnableAsync
@SpringBootApplication(scanBasePackages = "com.masikga")
@EnableJpaRepositories(basePackages = "com.masikga")
@EntityScan(basePackages = "com.masikga")
@ConfigurationPropertiesScan(basePackages = "com.masikga")
@MapperScan(basePackages = {
        "com.masikga.worldcupgame.domain.worldcup.repository.impl.mapper",
        "com.masikga.member.repository.impl.mapper"
})
public class ItwcApplication {

    public static void main(String[] args) {

        SpringApplication.run(ItwcApplication.class, args);

    }

}
