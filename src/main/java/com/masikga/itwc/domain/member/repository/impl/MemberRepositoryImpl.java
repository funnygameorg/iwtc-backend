package com.masikga.itwc.domain.member.repository.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.masikga.itwc.domain.member.repository.MemberQueryRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberRepositoryImpl implements MemberQueryRepository {

	private final JdbcTemplate jdbcTemplate;

	@Override
	public Boolean existsNickname(String nickname) {
		String sql = "SELECT EXISTS ( SELECT 1 FROM member m WHERE m.nickname = ?);";
		return jdbcTemplate.queryForObject(sql, Boolean.class, nickname);
	}

	@Override
	public Boolean existsServiceId(String serviceId) {
		String sql = "SELECT EXISTS ( SELECT 1 FROM member m WHERE m.service_id = ?);";
		return jdbcTemplate.queryForObject(sql, Boolean.class, serviceId);
	}

}
