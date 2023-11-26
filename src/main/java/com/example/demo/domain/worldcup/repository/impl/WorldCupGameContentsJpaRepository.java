package com.example.demo.domain.worldcup.repository.impl;

import com.example.demo.domain.worldcup.model.WorldCupGame;
import com.example.demo.domain.worldcup.model.WorldCupGameContents;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface WorldCupGameContentsJpaRepository extends JpaRepository<WorldCupGameContents, Long> {

    @EntityGraph(attributePaths = {"mediaFile"})
    List<WorldCupGameContents> findAllByWorldCupGame(WorldCupGame worldCupGame);

    @EntityGraph(attributePaths = {"mediaFile"})
    Optional<WorldCupGameContents> findById(Long worldCupGameContentsId);
}
