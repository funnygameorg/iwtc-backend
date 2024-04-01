package com.masikga.member;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.masikga")
@EnableJpaRepositories(basePackages = "com.masikga")
@MapperScan(basePackages = "com.masikga.member.repository.impl.mapper")
@EntityScan(basePackages = "com.masikga")
@ConfigurationPropertiesScan(basePackages = "com.masikga")
public class ItwcApplication {

    public static void main(String[] args) {
        SpringApplication.run(ItwcApplication.class, args);
    }

}
