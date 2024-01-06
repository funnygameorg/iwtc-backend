package com.example.demo.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.demo.common.component.RandomDataGenerator;
import com.example.demo.common.component.RandomDataGeneratorInterface;

@Configuration
public class UtilConfig {

	@Bean
	public RandomDataGeneratorInterface randomDataGeneratorInterface() {
		return new RandomDataGenerator();
	}
}
