package com.masikga.itwc.domain.worldcup.repository.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.masikga.itwc.domain.worldcup.model.WorldCupGame;
import com.masikga.itwc.domain.worldcup.model.WorldCupGameContents;

public interface WorldCupGameContentsJpaRepository extends JpaRepository<WorldCupGameContents, Long> {

	@EntityGraph(attributePaths = {"mediaFile"})
	List<WorldCupGameContents> findAllByWorldCupGame(WorldCupGame worldCupGame);

	@EntityGraph(attributePaths = {"mediaFile"})
	Optional<WorldCupGameContents> findById(Long worldCupGameContentsId);
}
