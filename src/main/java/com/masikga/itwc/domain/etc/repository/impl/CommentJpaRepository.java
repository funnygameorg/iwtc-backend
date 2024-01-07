package com.masikga.itwc.domain.etc.repository.impl;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.masikga.itwc.domain.etc.model.Comment;
import com.masikga.itwc.domain.worldcup.model.WorldCupGame;

public interface CommentJpaRepository extends JpaRepository<Comment, Long> {

	@EntityGraph(attributePaths = {"member"})
	List<Comment> findAllByWorldCupGame(WorldCupGame worldCupGame, PageRequest pageRequest);

}
