package com.example.demo.common.web.auth.rememberme;


import java.util.Optional;

// TODO : 운영하면서 REDIS, MEMCACHED, RDB 고려하기
public interface RememberMeRepository {
    Boolean isRemember(Long memberId);
    void save(Long MemberId);
    void signOut(String accessToken, Long memberId);
    Boolean containBlacklistedAccessToken(String accessToken);
}
