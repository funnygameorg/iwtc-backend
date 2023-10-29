package com.example.demo.domain.worldcup.repository.impl;

import com.example.demo.domain.worldcup.model.WorldCupGame;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorldCupGameJpaRepository extends JpaRepository<WorldCupGame, Long> { }
