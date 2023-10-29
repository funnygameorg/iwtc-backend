package com.example.demo.domain.worldcup.model.repository;

import com.example.demo.domain.worldcup.infra.WorldCupGameJpaRepository;
import com.example.demo.domain.worldcup.model.projection.GetAvailableGameRoundsProjection;

public interface WorldCupGameRepository extends WorldCupGameJpaRepository, WorldCupGameQueryRepository {
    void incrementView(Long worldCupGameId);

}
