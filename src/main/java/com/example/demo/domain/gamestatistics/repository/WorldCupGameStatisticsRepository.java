package com.example.demo.domain.gamestatistics.repository;

import com.example.demo.domain.gamestatistics.model.WorldCupGameStatistics;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorldCupGameStatisticsRepository extends JpaRepository<WorldCupGameStatistics, Long> {
}
