package com.masikga.itwc.domain.member.repository.impl;

import com.masikga.itwc.domain.member.repository.MemberQueryRepository;
import com.masikga.itwc.domain.member.repository.impl.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberRepositoryImpl implements MemberQueryRepository {

    private final JdbcTemplate jdbcTemplate;
    private final MemberMapper memberMapper;

    @Deprecated
    @Override
    public Boolean existsNickname(String nickname) {
        String sql = "SELECT EXISTS ( SELECT 1 FROM member m WHERE m.nickname = ?);";
        return jdbcTemplate.queryForObject(sql, Boolean.class, nickname);
    }

    @Deprecated
    @Override
    public Boolean existsServiceId(String serviceId) {
        String sql = "SELECT EXISTS ( SELECT 1 FROM member m WHERE m.service_id = ?);";
        return jdbcTemplate.queryForObject(sql, Boolean.class, serviceId);
    }

    @Override
    public Boolean existsNicknameV2(String nickname) {
        return memberMapper.existsNickname(nickname);
    }

    @Override
    public Boolean existsServiceIdV2(String serviceId) {
        return memberMapper.existsServiceId(serviceId);
    }

}
