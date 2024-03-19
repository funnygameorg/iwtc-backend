package com.masikga.member.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

@Component
public class CacheManagerCleanUp {

	@Autowired
	private CacheManager cacheManager;

	public void truncateAllCache() {

		cacheManager.getCacheNames().stream()
			.map(cacheManager::getCache)
			.forEach(Cache::invalidate);
	}

}
