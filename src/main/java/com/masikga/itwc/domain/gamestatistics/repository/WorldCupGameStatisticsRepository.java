package com.masikga.itwc.domain.gamestatistics.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.masikga.itwc.domain.gamestatistics.model.WorldCupGameStatistics;

public interface WorldCupGameStatisticsRepository extends JpaRepository<WorldCupGameStatistics, Long> {
}
