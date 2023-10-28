package com.example.demo.common.config;

import com.example.demo.common.cache.CacheType;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.util.Arrays.stream;

@Slf4j
@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public List<CaffeineCache> caffeineConfig() {
        return stream(CacheType.values())
                .map(cache -> new CaffeineCache(cache.getCacheName(),
                        Caffeine.newBuilder()
                                .recordStats()
                                .expireAfterWrite(
                                        cache.getExpireAfterWrite(),
                                        TimeUnit.SECONDS
                                )
                                .maximumSize(cache.getMaximumSize())
                                .build())
                )
                .toList();
    }

    @Bean
    public CacheManager cacheManager(List<CaffeineCache> caffeineCaches) {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(caffeineCaches);
        return cacheManager;
    }
}
