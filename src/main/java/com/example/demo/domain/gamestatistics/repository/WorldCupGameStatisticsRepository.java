package com.example.demo.domain.gamestatistics.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.domain.gamestatistics.model.WorldCupGameStatistics;

public interface WorldCupGameStatisticsRepository extends JpaRepository<WorldCupGameStatistics, Long> {
}
