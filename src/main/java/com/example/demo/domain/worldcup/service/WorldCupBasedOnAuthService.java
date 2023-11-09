package com.example.demo.domain.worldcup.service;

import com.example.demo.domain.worldcup.controller.response.GetWorldCupContentsResponse;
import com.example.demo.domain.worldcup.exception.NotFoundWorldCupGameException;
import com.example.demo.domain.worldcup.exception.NotOwnerGameException;
import com.example.demo.domain.worldcup.model.WorldCupGame;
import com.example.demo.domain.worldcup.model.WorldCupGameContents;
import com.example.demo.domain.worldcup.repository.WorldCupGameContentsRepository;
import com.example.demo.domain.worldcup.repository.WorldCupGameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WorldCupBasedOnAuthService {
    private final WorldCupGameRepository worldCupGameRepository;
    private final WorldCupGameContentsRepository worldCupGameContentsRepository;


    public List<GetWorldCupContentsResponse> getMyWorldCupGameContents(long worldCupId, long memberId) {

        WorldCupGame worldCupGame = worldCupGameRepository.findById(worldCupId).orElseThrow(
                () -> new NotFoundWorldCupGameException("%s 는 존재하지 않는 게임입니다.".formatted(worldCupId))
        );

        if(worldCupGame.getMemberId() != memberId) {
            throw new NotOwnerGameException();
        }

        List<WorldCupGameContents> contentsList = worldCupGameContentsRepository.findAllByWorldCupGame(worldCupGame);

        return contentsList.stream().map(GetWorldCupContentsResponse::fromEntity).toList();
    }
}
