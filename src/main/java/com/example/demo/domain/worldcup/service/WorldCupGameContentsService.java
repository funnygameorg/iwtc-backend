package com.example.demo.domain.worldcup.service;

import com.example.demo.domain.worldcup.controller.response.GetAvailableGameRoundsResponse;
import com.example.demo.domain.worldcup.exception.NoRoundsAvailableToPlayException;
import com.example.demo.domain.worldcup.exception.NotFoundWorldCupGameException;
import com.example.demo.domain.worldcup.model.vo.WorldCupGameRound;
import com.example.demo.domain.worldcup.repository.projection.GetAvailableGameRoundsProjection;
import com.example.demo.domain.worldcup.repository.WorldCupGameRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.Arrays.stream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WorldCupGameContentsService {

    private final WorldCupGameRepository worldCupGameRepository;

    public GetAvailableGameRoundsResponse getAvailableGameRounds(Long worldCupGameId) {

        if(!worldCupGameRepository.existsWorldCupGame(worldCupGameId)) {
            throw new NotFoundWorldCupGameException("%s 는 존재하지 않는 월드컵 게임입니다. ".formatted(worldCupGameId));
        }

        GetAvailableGameRoundsProjection result = worldCupGameRepository.getAvailableGameRounds(worldCupGameId);

        List<Integer> availableGameRounds = generateAvailableRounds(result.totalContentsSize());
        if(availableGameRounds.size() == 0) {
            throw new NoRoundsAvailableToPlayException(
                    "%s 의 응답으로는 게임을 할 수 없습니다.".formatted(result)
            );
        }

        worldCupGameRepository.incrementWorldCupGameViews(worldCupGameId);

        return new GetAvailableGameRoundsResponse(
                result.worldCupId(),
                result.worldCupTitle(),
                result.worldCupDescription(),
                availableGameRounds
                );
    }

    private List<Integer> generateAvailableRounds(Long ContentsSize) {
        return stream(WorldCupGameRound.values())
                .filter(gameRound -> gameRound.isAvailableRound(ContentsSize))
                .map(availableRound -> availableRound.roundValue)
                .toList();
    }
}
