package com.masikga.itwc.common.error.entity;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ErrorCodeRepository extends JpaRepository<ErrorCodeEntity, Long> {
	Optional<ErrorCodeEntity> findByName(String name);
}
