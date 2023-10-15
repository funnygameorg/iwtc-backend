package com.example.demo.member.repository;

import com.example.demo.member.model.MemberQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberRepositoryImpl implements MemberQueryRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public int existsNickname(String nickname) {
        String sql = "SELECT EXISTS ( SELECT 1 FROM member WHERE member.nickname = ?);";
        return jdbcTemplate.queryForObject(sql, Integer.class, nickname);
    }

    @Override
    public int existsServiceId(String serviceId) {
        String sql = "SELECT EXISTS ( SELECT 1 FROM member WHERE member.service_id = ?);";
        return jdbcTemplate.queryForObject(sql, Integer.class, serviceId);
    }
}
