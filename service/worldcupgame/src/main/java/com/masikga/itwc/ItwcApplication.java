package com.masikga.itwc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
@ConfigurationPropertiesScan(basePackages = {"com.masikga.itwc.common.config.property"})
public class ItwcApplication {

	public static void main(String[] args) {
		SpringApplication.run(ItwcApplication.class, args);
	}

}
