package com.example.demo.common.web.auth.rememberme.impl.rdb;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

interface RememberMeJpaRepository extends JpaRepository<RDBRememberMe, Long> {
    void deleteByMemberId(Long memberId);

    Optional<RDBRememberMe> findByMemberId(Long memberId);

    List<RDBRememberMe> findAllByMemberId(Long memberId);

}
