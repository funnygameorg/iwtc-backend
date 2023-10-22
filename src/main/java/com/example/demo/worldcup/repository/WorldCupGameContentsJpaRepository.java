package com.example.demo.worldcup.repository;

import com.example.demo.worldcup.model.entity.WorldCupGameContents;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorldCupGameContentsJpaRepository extends JpaRepository<WorldCupGameContents, Long> {
}
