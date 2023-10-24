package com.example.demo.domain.worldcup.repository;

import com.example.demo.domain.worldcup.model.entity.WorldCupGameContents;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorldCupGameContentsJpaRepository extends JpaRepository<WorldCupGameContents, Long> {
}
