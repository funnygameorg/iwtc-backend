package com.example.demo.domain.worldcup.service;

import com.example.demo.domain.worldcup.controller.request.ClearWorldCupGameRequest;
import com.example.demo.domain.worldcup.controller.response.ClearWorldCupGameResponse;
import com.example.demo.domain.worldcup.controller.response.GetAvailableGameRoundsResponse;
import com.example.demo.domain.worldcup.controller.response.GetGameResultContentsListResponse;
import com.example.demo.domain.worldcup.controller.response.GetWorldCupPlayContentsResponse;
import com.example.demo.domain.worldcup.exception.*;
import com.example.demo.domain.worldcup.model.WorldCupGame;
import com.example.demo.domain.worldcup.model.WorldCupGameContents;
import com.example.demo.domain.worldcup.model.vo.WorldCupGameRound;
import com.example.demo.domain.worldcup.repository.WorldCupGameContentsRepository;
import com.example.demo.domain.worldcup.repository.projection.GetAvailableGameRoundsProjection;
import com.example.demo.domain.worldcup.repository.WorldCupGameRepository;
import com.example.demo.domain.worldcup.repository.projection.GetDividedWorldCupGameContentsProjection;
import com.google.common.base.Objects;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

import static com.google.common.base.Objects.equal;
import static com.google.common.collect.Sets.newHashSet;
import static java.util.Arrays.stream;
import static java.util.Comparator.comparing;
import static java.util.stream.IntStream.range;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WorldCupGameContentsService {

    private final WorldCupGameRepository worldCupGameRepository;
    private final WorldCupGameContentsRepository worldCupGameContentsRepository;





    public GetAvailableGameRoundsResponse getAvailableGameRounds(Long worldCupGameId) {

        if(!worldCupGameRepository.existsWorldCupGame(worldCupGameId)) {

            throw new NotFoundWorldCupGameException(worldCupGameId);

        }

        GetAvailableGameRoundsProjection result = worldCupGameRepository.getAvailableGameRounds(worldCupGameId);
        List<Integer> availableGameRounds = generateAvailableRounds(result.totalContentsSize());


        if(availableGameRounds.size() == 0) {

            throw new NoRoundsAvailableToPlayException(result.toString());

        }

        worldCupGameRepository.incrementWorldCupGameViews(worldCupGameId);

        return new GetAvailableGameRoundsResponse(
                result.worldCupId(),
                result.worldCupTitle(),
                result.worldCupDescription(),
                availableGameRounds
        );
    }





    // 제공된 컨텐츠 수로 플레이할 수 있는 라운드 수의 크기
    private List<Integer> generateAvailableRounds(Long ContentsSize) {

        return stream(WorldCupGameRound.values())
                .filter(gameRound -> gameRound.isAvailableRound(ContentsSize))
                .map(availableRound -> availableRound.roundValue)
                .toList();

    }






    public GetWorldCupPlayContentsResponse getPlayContents(
            Long worldCupGameId,
            int currentRound,
            int divideContentsSizePerRequest,
            List<Long> alreadyPlayedContentsIds
    ) {

        WorldCupGame worldCupGame = worldCupGameRepository
                .findById(worldCupGameId)
                .orElseThrow(() -> new NotFoundWorldCupGameException(worldCupGameId));

        WorldCupGameRound worldCupGameRound = WorldCupGameRound.getRoundFromValue(currentRound);

        int wantedContentsSize = worldCupGameRound.getGameContentsSizePerRequest(divideContentsSizePerRequest);

        List<GetDividedWorldCupGameContentsProjection> contentsProjections =  worldCupGameRepository
                .getDividedWorldCupGameContents(
                        worldCupGameId,
                        wantedContentsSize,
                        alreadyPlayedContentsIds
                );

        if(equalsExpectedContentsSize(wantedContentsSize, contentsProjections.size())) {

            throw new IllegalWorldCupGameContentsException(
                    "조회 컨텐츠 수가 다름 %s, %s".formatted(wantedContentsSize, contentsProjections.size())
            );

        }

        if(containsAlreadyContents(alreadyPlayedContentsIds, contentsProjections)) {

                throw new IllegalWorldCupGameContentsException("컨텐츠 중복 : 이미 플레이한 컨텐츠 %s, 조회 성공 컨텐츠 %s"
                        .formatted(alreadyPlayedContentsIds, contentsProjections)
                );

        }

        return GetWorldCupPlayContentsResponse.fromProjection(
                worldCupGame.getId(),
                worldCupGame.getTitle(),
                worldCupGameRound.roundValue,
                contentsProjections
        );

    }







    // 조회하기를 원하는 컨텐츠 수만큼 조회했는가?
    private boolean equalsExpectedContentsSize(int expectedContentsSize, int actualContentsSize) {
        return expectedContentsSize != actualContentsSize;
    }







    // 이미 조회한 컨텐츠를 포함하는가?
    private boolean containsAlreadyContents(
            List<Long> alreadyPlayedContentsIds,
            List<GetDividedWorldCupGameContentsProjection> contentsProjections
    ) {

        return contentsProjections.stream()
                .map(GetDividedWorldCupGameContentsProjection::contentsId)
                .anyMatch(alreadyPlayedContentsIds::contains);

    }

    




    public List<ClearWorldCupGameResponse> clearWorldCupGame(long worldCupId, ClearWorldCupGameRequest request) {

        var contents = worldCupGameContentsRepository.findAllById(request.getWinnerIds());

        if(contents.size() != 4) {
            throw new NotFoundWorldCupContentsException("조회 실패 개수 " + (4 - contents.size()));
        }


        worldCupGameContentsRepository.saveWinnerContentsScore(worldCupId, request.firstWinnerContentsId(), 10);
        worldCupGameContentsRepository.saveWinnerContentsScore(worldCupId, request.secondWinnerContentsId(), 7);
        worldCupGameContentsRepository.saveWinnerContentsScore(worldCupId, request.thirdWinnerContentsId(), 4);
        worldCupGameContentsRepository.saveWinnerContentsScore(worldCupId, request.fourthWinnerContentsId(), 4);


        return getClearWorldCupGameResponses(request, contents);

    }





    // 조회한 컨텐츠들을 랭크별로 정렬한다.
    // TODO : `request`의 순위 객체를 배열에 넣고 코드 작성하기
    private List<ClearWorldCupGameResponse> getClearWorldCupGameResponses(ClearWorldCupGameRequest request, List<WorldCupGameContents> contents) {

        var firstContents = ClearWorldCupGameResponse.fromEntity(
                contents.stream()
                        .filter(item -> item.getId() == request.firstWinnerContentsId())
                        .findFirst()
                        .orElse(null),
                1
        );

        var secondContents = ClearWorldCupGameResponse.fromEntity(
                contents.stream()
                        .filter(item -> item.getId() == request.secondWinnerContentsId())
                        .findFirst()
                        .orElse(null),
                2
        );

        var thirdContents = ClearWorldCupGameResponse.fromEntity(
                contents.stream()
                        .filter(item -> item.getId() == request.thirdWinnerContentsId())
                        .findFirst().orElse(null),
                3
        );

        var fourthContents = ClearWorldCupGameResponse.fromEntity(
                contents.stream()
                        .filter(item -> item.getId() == request.fourthWinnerContentsId())
                        .findFirst()
                        .orElse(null),
                4
        );


        return List.of(firstContents, secondContents, thirdContents, fourthContents);
    }







    public List<GetGameResultContentsListResponse> getGameResultContentsList(Long worldCupId) {

        var worldCupGame = worldCupGameRepository.findById(worldCupId)
                .orElseThrow(() -> new NotFoundWorldCupGameException(worldCupId));

        var contentsList = worldCupGameContentsRepository.findAllByWorldCupGame(worldCupGame);

        return contentsList.stream()
                .sorted(comparing(WorldCupGameContents::getGameRank))
                .map(GetGameResultContentsListResponse::fromEntity)
                .toList();

    }




}
