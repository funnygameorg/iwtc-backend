package com.masikga.itwc.common.config;

import static java.util.Arrays.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.masikga.itwc.common.cache.CacheType;
import com.github.benmanes.caffeine.cache.Caffeine;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableCaching
public class CacheConfig {

	/*
		구현체 카페인 사용
	 */
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
