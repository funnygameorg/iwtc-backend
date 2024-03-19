package com.masikga.worldcupgame.common.util;

import java.util.UUID;

public class RandomDataGenerator implements RandomDataGeneratorInterface {

	@Override
	public String generate() {
		return UUID.randomUUID().toString();
	}
}
