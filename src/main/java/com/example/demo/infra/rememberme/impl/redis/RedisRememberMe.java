package com.example.demo.infra.rememberme.impl.redis;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.Getter;

@Getter
@RedisHash(value = "rememberMe", timeToLive = 21600000)
public class RedisRememberMe {
	@Id
	private String memberId;
}
