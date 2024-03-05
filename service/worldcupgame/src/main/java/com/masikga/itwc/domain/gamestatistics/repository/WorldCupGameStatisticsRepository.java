package com.masikga.itwc.domain.gamestatistics.repository;

import com.masikga.itwc.domain.gamestatistics.model.WorldCupGameStatistics;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorldCupGameStatisticsRepository extends JpaRepository<WorldCupGameStatistics, Long> {
}
