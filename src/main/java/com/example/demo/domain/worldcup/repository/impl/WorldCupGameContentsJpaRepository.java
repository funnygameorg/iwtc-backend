package com.example.demo.domain.worldcup.repository.impl;

import com.example.demo.domain.worldcup.model.WorldCupGame;
import com.example.demo.domain.worldcup.model.WorldCupGameContents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WorldCupGameContentsJpaRepository extends JpaRepository<WorldCupGameContents, Long> {

    @Query(value = "SELECT")
    List<WorldCupGameContents> findAllByWorldCupGame(WorldCupGame worldCupGame);
}
