package com.masikga.member.infra.rememberme.impl.rdb;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RememberMeJpaRepository extends JpaRepository<RDBRememberMe, Long> {
    void deleteByMemberId(Long memberId);

    Optional<RDBRememberMe> findByMemberId(Long memberId);

    List<RDBRememberMe> findAllByMemberId(Long memberId);

}
