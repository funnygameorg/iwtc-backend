package com.example.demo.domain.worldcup.repository.impl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.domain.worldcup.model.WorldCupGame;

public interface WorldCupGameJpaRepository extends JpaRepository<WorldCupGame, Long> {

	boolean existsByTitle(String title);

	List<WorldCupGame> findAllByMemberId(Long memberId);

}
