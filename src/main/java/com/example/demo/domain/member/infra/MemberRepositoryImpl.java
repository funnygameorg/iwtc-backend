package com.example.demo.domain.member.infra;

import com.example.demo.domain.member.model.repository.MemberQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
    public Optional<Long> findByMemberIdByServiceIdAndPassword(String serviceId, String password) {
        String sql = "SELECT m.id FROM member m WHERE m.service_id = ? AND m.password = ?";
        List<Long> result = jdbcTemplate.query(
                sql,
                (rs, rowNum) -> rs.getLong("id"),
                serviceId,
                password
        );
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }
}
