package com.example.demo.domain.worldcup.service;

import com.example.demo.domain.etc.component.MediaFileFactory;
import com.example.demo.domain.etc.model.MediaFile;
import com.example.demo.domain.etc.repository.MediaFileRepository;
import com.example.demo.domain.worldcup.controller.request.CreateWorldCupContentsRequest;
import com.example.demo.domain.worldcup.controller.request.CreateWorldCupRequest;
import com.example.demo.domain.worldcup.controller.response.GetWorldCupContentsResponse;
import com.example.demo.domain.worldcup.exception.DuplicatedWorldCupGameTitleException;
import com.example.demo.domain.worldcup.exception.NotFoundWorldCupGameException;
import com.example.demo.domain.worldcup.exception.NotOwnerGameException;
import com.example.demo.domain.worldcup.model.WorldCupGame;
import com.example.demo.domain.worldcup.model.WorldCupGameContents;
import com.example.demo.domain.worldcup.repository.WorldCupGameContentsRepository;
import com.example.demo.domain.worldcup.repository.WorldCupGameRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WorldCupBasedOnAuthService {
    private final WorldCupGameRepository worldCupGameRepository;
    private final WorldCupGameContentsRepository worldCupGameContentsRepository;
    private final MediaFileRepository mediaFileRepository;

    private final MediaFileFactory mediaFileFactory;





    public List<GetWorldCupContentsResponse> getMyWorldCupGameContents(long worldCupId, long memberId) {

        WorldCupGame worldCupGame = worldCupGameRepository
                .findById(worldCupId)
                .orElseThrow(() ->
                        new NotFoundWorldCupGameException("%s 는 존재하지 않는 게임입니다.".formatted(worldCupId))
                );

        if(worldCupGame.getMemberId() != memberId) {
            throw new NotOwnerGameException();
        }

        return worldCupGameContentsRepository.findAllByWorldCupGame(worldCupGame)
                .stream()
                .map(GetWorldCupContentsResponse::fromEntity)
                .toList();

    }





    @Transactional
    public void createMyWorldCup(CreateWorldCupRequest request, Long memberId) {

        if(existsGameTitle(request)) {
            throw new DuplicatedWorldCupGameTitleException(request.title());
        }

        WorldCupGame newGame = request.toEntity(memberId);

        worldCupGameRepository.save(newGame);

    }





    @Transactional
    public void putMyWorldCup(CreateWorldCupRequest request, Long worldCupId, Long memberId) {

        if(existsGameTitle(request)) {
            throw new DuplicatedWorldCupGameTitleException(request.title());
        }

        WorldCupGame worldCupGame = worldCupGameRepository
                .findById(worldCupId)
                .orElseThrow(() -> new NotFoundWorldCupGameException(worldCupId));

        if(isGameOwner(memberId, worldCupGame)) {
            throw new NotOwnerGameException();
        }

        worldCupGame.simpleUpdate(
                request.title(),
                request.description(),
                request.visibleType()
        );

    }





    // 해당 `WorldCupGame`의 작성자인가?
    private boolean isGameOwner(Long memberId, WorldCupGame worldCupGame) {

        return worldCupGame.getMemberId() != memberId;

    }




    // 이미 존재하는 `WorldCupGame Title`인가?
    private boolean existsGameTitle(CreateWorldCupRequest request) {

        return worldCupGameRepository.existsByTitle(request.title());

    }





    @Transactional
    public void createMyWorldCupContents(CreateWorldCupContentsRequest request, long WorldCupId, long memberId) {

        WorldCupGame worldCupGame = worldCupGameRepository
                .findById(WorldCupId)
                .orElseThrow(() -> new NotFoundWorldCupGameException(WorldCupId));

        if(!worldCupGame.isOwner(memberId)) {
            throw new NotOwnerGameException();
        }

        List<WorldCupGameContents> newContentsList = createNewContentsList(request, worldCupGame);
        List<MediaFile> newMediaFiles = newContentsList.stream().map(contents -> contents.getMediaFile()).toList();

        mediaFileRepository.saveAll(newMediaFiles);
        worldCupGameContentsRepository.saveAll(newContentsList);

    }





    private List<WorldCupGameContents> createNewContentsList(
            CreateWorldCupContentsRequest request,
            WorldCupGame worldCupGame
    ) {

        return request.data().stream()
                .map(contentsRequest -> {
                            CreateWorldCupContentsRequest.CreateMediaFileRequest mediaFileRequest =
                                    contentsRequest.createMediaFileRequest();

                            MediaFile newMediaFile = mediaFileFactory.createMediaFile(
                                    mediaFileRequest.fileType(),
                                    mediaFileRequest.mediaPath(),
                                    mediaFileRequest.originalName(),
                                    mediaFileRequest.absoluteName(),
                                    mediaFileRequest.videoPlayDuration(),
                                    mediaFileRequest.videoStartTime()
                            );

                            return WorldCupGameContents.createNewContents(
                                    worldCupGame,
                                    newMediaFile,
                                    contentsRequest.contentsName(),
                                    contentsRequest.visibleType()
                            );
                        }
                ).toList();

    }


}
