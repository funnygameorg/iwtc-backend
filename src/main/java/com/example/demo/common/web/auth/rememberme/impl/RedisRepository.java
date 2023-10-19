package com.example.demo.common.web.auth.rememberme.impl;

import com.example.demo.common.web.auth.rememberme.RememberMe;
import com.example.demo.common.web.auth.rememberme.RememberMeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class RedisRepository implements RememberMeRepository {

    private final RedisTemplate<String, Object> template;
    private final String REMEMBER_KEY = "remember";
    @Override
    public void save(Long memberId) {
        // SADD
        Long result =template.opsForSet().add(REMEMBER_KEY, memberId.toString());
    }

    @Override
    public Boolean isRemember(Long memberId) {
        // SISMEMBER
        return template.opsForSet().isMember(REMEMBER_KEY, memberId.toString());
    }
}
