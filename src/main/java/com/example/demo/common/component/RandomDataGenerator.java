package com.example.demo.common.component;

import java.util.UUID;

public class RandomDataGenerator implements RandomDataGeneratorInterface {

	@Override
	public String generate() {
		return UUID.randomUUID().toString();
	}
}
