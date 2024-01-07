package com.masikga.itwc.infra.rememberme.impl.rdb;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RememberMeJpaRepository extends JpaRepository<RDBRememberMe, Long> {
	void deleteByMemberId(Long memberId);

	Optional<RDBRememberMe> findByMemberId(Long memberId);

	List<RDBRememberMe> findAllByMemberId(Long memberId);

}
