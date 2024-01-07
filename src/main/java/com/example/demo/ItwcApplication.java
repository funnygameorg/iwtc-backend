package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
@ConfigurationPropertiesScan(basePackages = {"com.example.demo.common.config.property"})
public class ItwcApplication {

	public static void main(String[] args) {
		SpringApplication.run(ItwcApplication.class, args);
	}

}
