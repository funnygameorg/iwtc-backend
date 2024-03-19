package com.masikga.member.infra.rememberme.impl.redis;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value = "rememberMe", timeToLive = 21600000)
public class RedisRememberMe {
	@Id
	private String memberId;
}
