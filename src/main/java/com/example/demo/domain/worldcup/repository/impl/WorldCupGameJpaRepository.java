package com.example.demo.domain.worldcup.repository.impl;

import com.example.demo.domain.worldcup.model.WorldCupGame;
import com.example.demo.domain.worldcup.model.WorldCupGameContents;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorldCupGameJpaRepository extends JpaRepository<WorldCupGame, Long> {

    boolean existsByTitle(String title);

    List<WorldCupGame> findAllByMemberId(Long memberId);
}
