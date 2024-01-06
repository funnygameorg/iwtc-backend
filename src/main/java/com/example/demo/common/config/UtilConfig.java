package com.example.demo.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.demo.common.util.RandomDataGenerator;
import com.example.demo.common.util.RandomDataGeneratorInterface;

@Configuration
public class UtilConfig {

	@Bean
	public RandomDataGeneratorInterface randomDataGeneratorInterface() {
		return new RandomDataGenerator();
	}
}
