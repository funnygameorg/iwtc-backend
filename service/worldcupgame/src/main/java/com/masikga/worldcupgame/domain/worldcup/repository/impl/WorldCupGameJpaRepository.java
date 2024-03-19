package com.masikga.worldcupgame.domain.worldcup.repository.impl;

import com.masikga.worldcupgame.domain.worldcup.model.WorldCupGame;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorldCupGameJpaRepository extends JpaRepository<WorldCupGame, Long> {

    boolean existsByTitle(String title);

    List<WorldCupGame> findAllByMemberId(Long memberId);

}
