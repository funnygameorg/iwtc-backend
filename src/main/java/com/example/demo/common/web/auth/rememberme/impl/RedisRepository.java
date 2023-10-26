package com.example.demo.common.web.auth.rememberme.impl;

import com.example.demo.common.web.auth.rememberme.RememberMeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Repository;

import static java.util.concurrent.TimeUnit.*;

@Slf4j
@Repository
@RequiredArgsConstructor
public class RedisRepository implements RememberMeRepository {

    private final RedisTemplate<String, Object> template;
    private final String REMEMBER_KEY = "remember";
    private final String BLACK_ACCESS_TOKEN_LIST = "black";
    @Override
    public void save(Long memberId) {
        // SADD
        template.opsForSet().add(REMEMBER_KEY, memberId.toString());
        template.expire(REMEMBER_KEY, 21600000, MILLISECONDS);
    }

    @Override
    public Boolean isRemember(Long memberId) {
        // SISMEMBER
        return template.opsForSet().isMember(REMEMBER_KEY, memberId.toString());
    }

    @Override
    public void signOut(String accessToken, Long memberId) {
        SetOperations<String, Object> ops = template.opsForSet();
        // SREM
        ops.remove(REMEMBER_KEY, memberId.toString());
        ops.add(BLACK_ACCESS_TOKEN_LIST, accessToken);
        template.expire(BLACK_ACCESS_TOKEN_LIST, 20000, MILLISECONDS);
    }

    @Override
    public Boolean containBlacklistedAccessToken(String accessToken) {
        return template.opsForSet().isMember(BLACK_ACCESS_TOKEN_LIST, accessToken);
    }
}
