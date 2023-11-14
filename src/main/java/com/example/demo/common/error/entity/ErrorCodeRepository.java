package com.example.demo.common.error.entity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ErrorCodeRepository extends JpaRepository<ErrorCodeEntity, Long> {
    Optional<ErrorCodeEntity> findByName(String name);
}
