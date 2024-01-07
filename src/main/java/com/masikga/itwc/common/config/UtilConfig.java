package com.masikga.itwc.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.masikga.itwc.common.util.RandomDataGenerator;
import com.masikga.itwc.common.util.RandomDataGeneratorInterface;

@Configuration
public class UtilConfig {

	@Bean
	public RandomDataGeneratorInterface randomDataGeneratorInterface() {
		return new RandomDataGenerator();
	}
}
