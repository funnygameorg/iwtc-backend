package com.example.demo.domain.worldcup.infra;

import com.example.demo.domain.worldcup.model.entity.WorldCupGame;
import com.example.demo.domain.worldcup.model.projection.GetWorldCupGamePageProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface WorldCupGameJpaRepository extends JpaRepository<WorldCupGame, Long> { }
