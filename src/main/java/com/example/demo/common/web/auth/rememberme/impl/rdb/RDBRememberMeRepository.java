package com.example.demo.common.web.auth.rememberme.impl.rdb;

import static java.lang.Boolean.*;
import static java.time.LocalDateTime.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.common.web.auth.rememberme.RememberMeRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RDBRememberMeRepository implements RememberMeRepository {

	private final RememberMeJpaRepository rememberMeJpaRepository;
	private final EntityManager em;

	@Value("${jwt.token-validity-milli-seconds.refresh}")
	private Long refreshTokenValiditySeconds;

	@Value("${jwt.token-validity-milli-seconds.access}")
	private Long accessTokenValiditySeconds;

	@Transactional
	@Override
	public Long removeRemember(Long memberId) {

		var oldRemembers = rememberMeJpaRepository.findAllByMemberId(memberId);
		rememberMeJpaRepository.deleteAll(oldRemembers);
		return memberId;

	}

	@Override
	public Boolean isRemember(Long memberId) {
		var optionalRDBRememberMe = rememberMeJpaRepository.findByMemberId(memberId);

		if (optionalRDBRememberMe.isEmpty()) {
			return false;
		}

		return !optionalRDBRememberMe.get().isExpired(now(), refreshTokenValiditySeconds);

	}

	@Transactional
	@Override
	public void save(Long memberId) {

		var rdbRememberMe = RDBRememberMe.builder()
			.memberId(memberId)
			.rememberAt(now())
			.build();

		rememberMeJpaRepository.deleteByMemberId(memberId);
		rememberMeJpaRepository.save(rdbRememberMe);

	}

	@Transactional
	@Override
	public void signOut(String accessToken, Long memberId) {

		rememberMeJpaRepository.deleteByMemberId(memberId);
		em.createNativeQuery("INSERT INTO black_access_token(access_token, registered_at) VALUES (:accessToken, :now)")
			.setParameter("accessToken", accessToken)
			.setParameter("now", now())
			.executeUpdate();

	}

	@Override
	public Boolean containBlacklistedAccessToken(String accessToken) {

		Query query = em.createQuery(
				"SELECT EXISTS ( SELECT 1 FROM BlackAccessToken b WHERE accessToken = :accessToken )")
			.setParameter("accessToken", accessToken);

		return booleanValueConvert(query.getSingleResult());

	}

	/*
		TODO : H2, MySQL DB Boolean 타입 호환으로 임시 사용
	 */
	private Boolean booleanValueConvert(Object booleanTypeValue) {
		if (booleanTypeValue instanceof Boolean) {
			return (Boolean)booleanTypeValue;
		}
		return (Long)booleanTypeValue == 1 ? TRUE : FALSE;
	}
}
