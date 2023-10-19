package com.example.demo.common.web.auth.rememberme;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value = "rememberMe", timeToLive = 21600000)
public class RememberMe {
    @Id
    private String memberId;
}
