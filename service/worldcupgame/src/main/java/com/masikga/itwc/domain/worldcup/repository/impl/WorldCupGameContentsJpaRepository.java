package com.masikga.itwc.domain.worldcup.repository.impl;

import com.masikga.itwc.domain.worldcup.model.WorldCupGame;
import com.masikga.itwc.domain.worldcup.model.WorldCupGameContents;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WorldCupGameContentsJpaRepository extends JpaRepository<WorldCupGameContents, Long> {

    @EntityGraph(attributePaths = {"mediaFile"})
    List<WorldCupGameContents> findAllByWorldCupGame(WorldCupGame worldCupGame);

    @EntityGraph(attributePaths = {"mediaFile"})
    Optional<WorldCupGameContents> findById(Long worldCupGameContentsId);
}
