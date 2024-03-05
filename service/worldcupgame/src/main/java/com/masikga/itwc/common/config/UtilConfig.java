package com.masikga.itwc.common.config;

import com.masikga.itwc.common.util.RandomDataGenerator;
import com.masikga.itwc.common.util.RandomDataGeneratorInterface;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UtilConfig {

	@Bean
	public RandomDataGeneratorInterface randomDataGeneratorInterface() {
		return new RandomDataGenerator();
	}
}
