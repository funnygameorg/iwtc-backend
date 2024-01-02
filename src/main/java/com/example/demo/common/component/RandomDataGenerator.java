package com.example.demo.common.component;

import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class RandomDataGenerator implements RandomDataGeneratorInterface {

	@Override
	public String generate() {
		return UUID.randomUUID().toString();
	}
}
