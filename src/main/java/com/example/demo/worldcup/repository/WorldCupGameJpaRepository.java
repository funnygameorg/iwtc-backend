package com.example.demo.worldcup.repository;

import com.example.demo.worldcup.model.WorldCupGame;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface WorldCupGameJpaRepository extends JpaRepository<WorldCupGame, Long> {

    @Query("")
    Page<WorldCupGame> findWorldCupGamePage(
            @Param("keyword") String keyword,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable
            );
}
