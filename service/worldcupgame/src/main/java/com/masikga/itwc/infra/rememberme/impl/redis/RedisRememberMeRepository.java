package com.masikga.itwc.infra.rememberme.impl.redis;

import com.masikga.itwc.infra.rememberme.RememberMeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Slf4j
@RequiredArgsConstructor
public class RedisRememberMeRepository implements RememberMeRepository {

	private final RedisTemplate<String, Object> template;
	private final String REMEMBER_KEY = "remember";
	private final String BLACK_ACCESS_TOKEN_LIST = "black";

	@Value("${jwt.token-validity-milli-seconds.refresh}")
	private Long refreshTokenValiditySeconds;

	@Value("${jwt.token-validity-milli-seconds.access}")
	private Long accessTokenValiditySeconds;

	@Override
	public void save(Long memberId) {
		// SADD
		template.opsForSet().add(REMEMBER_KEY, memberId.toString());
		template.expire(REMEMBER_KEY, refreshTokenValiditySeconds, MILLISECONDS);
	}

	@Override
	public Long removeRemember(Long memberId) {
		SetOperations<String, Object> ops = template.opsForSet();
		// SREM
		return ops.remove(REMEMBER_KEY, memberId.toString());

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
		template.expire(BLACK_ACCESS_TOKEN_LIST, accessTokenValiditySeconds, MILLISECONDS);
	}

	@Override
	public Boolean containBlacklistedAccessToken(String accessToken) {
		return template.opsForSet().isMember(BLACK_ACCESS_TOKEN_LIST, accessToken);
	}
}
