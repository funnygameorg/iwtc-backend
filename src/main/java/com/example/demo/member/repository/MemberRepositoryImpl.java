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
    public Boolean existsNickname(String nickname) {
        String sql = "SELECT EXISTS ( SELECT 1 FROM member m WHERE m.nickname = ?);";
        return jdbcTemplate.queryForObject(sql, Boolean.class, nickname);
    }

    @Override
    public Boolean existsServiceId(String serviceId) {
        String sql = "SELECT EXISTS ( SELECT 1 FROM member m WHERE m.service_id = ?);";
        return jdbcTemplate.queryForObject(sql, Boolean.class, serviceId);
    }

    @Override
    public Boolean existsMemberWithServiceIdAndPassword(String serviceId, String password) {
        String sql = "SELECT EXISTS ( SELECT 1 FROM member m WHERE m.service_id = ? AND m.password = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, serviceId, password);
    }
}
