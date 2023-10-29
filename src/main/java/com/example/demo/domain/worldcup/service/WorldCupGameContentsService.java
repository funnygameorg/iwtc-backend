package com.example.demo.domain.worldcup.service;

import com.example.demo.domain.worldcup.controller.response.getAvailableGameRoundsResponse;
import com.example.demo.domain.worldcup.exception.NotFoundWorldCupGame;
import com.example.demo.domain.worldcup.model.projection.GetAvailableGameRoundsProjection;
import com.example.demo.domain.worldcup.model.repository.WorldCupGameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WorldCupGameContentsService {

    private final WorldCupGameRepository worldCupGameRepository;

    public getAvailableGameRoundsResponse getAvailableGameRounds(Long worldCupGameId) {

        if(!worldCupGameRepository.existsWorldGame(worldCupGameId)) {
            throw new NotFoundWorldCupGame("%s 는 존재하지 않는 월드컵 게임입니다. ".formatted(worldCupGameId));
        }

        GetAvailableGameRoundsProjection result = worldCupGameRepository.getAvailableGameRounds(worldCupGameId);
        worldCupGameRepository.incrementView(worldCupGameId);

        return new getAvailableGameRoundsResponse(null, null, null, null);
    }
}
