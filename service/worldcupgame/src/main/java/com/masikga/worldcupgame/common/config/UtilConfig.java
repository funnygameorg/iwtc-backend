package com.masikga.worldcupgame.common.config;

import com.masikga.worldcupgame.common.util.RandomDataGenerator;
import com.masikga.worldcupgame.common.util.RandomDataGeneratorInterface;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UtilConfig {

	@Bean
	public RandomDataGeneratorInterface randomDataGeneratorInterface() {
		return new RandomDataGenerator();
	}
}
