package com.masikga.worldcupgame.domain.worldcup.service;

import com.masikga.feign.MemberClient;
import com.masikga.model.common.RestApiResponse;
import com.masikga.model.member.GetMemberResponse;
import com.masikga.worldcupgame.common.util.RandomDataGeneratorInterface;
import com.masikga.worldcupgame.domain.etc.component.MediaFileFactory;
import com.masikga.worldcupgame.domain.etc.model.MediaFile;
import com.masikga.worldcupgame.domain.etc.model.vo.FileType;
import com.masikga.worldcupgame.domain.etc.repository.MediaFileRepository;
import com.masikga.worldcupgame.domain.worldcup.controller.request.CreateWorldCupContentsRequest;
import com.masikga.worldcupgame.domain.worldcup.controller.request.CreateWorldCupRequest;
import com.masikga.worldcupgame.domain.worldcup.controller.request.UpdateWorldCupContentsRequest;
import com.masikga.worldcupgame.domain.worldcup.controller.response.GetMyWorldCupContentsResponse;
import com.masikga.worldcupgame.domain.worldcup.controller.response.GetMyWorldCupResponse;
import com.masikga.worldcupgame.domain.worldcup.controller.response.GetWorldCupContentsResponse;
import com.masikga.worldcupgame.domain.worldcup.controller.response.GetWorldCupResponse;
import com.masikga.worldcupgame.domain.worldcup.exception.DuplicatedWorldCupGameTitleExceptionMember;
import com.masikga.worldcupgame.domain.worldcup.exception.NotFoundWorldCupContentsExceptionMember;
import com.masikga.worldcupgame.domain.worldcup.exception.NotFoundWorldCupGameExceptionMember;
import com.masikga.worldcupgame.domain.worldcup.exception.NotOwnerGameExceptionMember;
import com.masikga.worldcupgame.domain.worldcup.model.WorldCupGame;
import com.masikga.worldcupgame.domain.worldcup.model.WorldCupGameContents;
import com.masikga.worldcupgame.domain.worldcup.repository.WorldCupGameContentsRepository;
import com.masikga.worldcupgame.domain.worldcup.repository.WorldCupGameRepository;
import com.masikga.worldcupgame.infra.filestorage.FileStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WorldCupBasedOnAuthService {

    private final WorldCupGameRepository worldCupGameRepository;
    private final WorldCupGameContentsRepository worldCupGameContentsRepository;
    private final MediaFileRepository mediaFileRepository;
    private final MediaFileFactory mediaFileFactory;
    private final RandomDataGeneratorInterface randomDataGenerator;
    private final FileStorage fileStorage;

    private final MemberClient memberClient;

    public List<GetWorldCupContentsResponse> getMyWorldCupGameContents(long worldCupId, long memberId) {

        RestApiResponse<GetMemberResponse> getMemberResponse = memberClient.findMember(100);

        WorldCupGame worldCupGame = worldCupGameRepository
                .findById(worldCupId)
                .orElseThrow(() ->
                        new NotFoundWorldCupGameExceptionMember("%s 는 존재하지 않는 게임입니다.".formatted(worldCupId))
                );

        if (!worldCupGame.isOwner(memberId)) {
            throw new NotOwnerGameExceptionMember();
        }

        return worldCupGameContentsRepository.findAllByWorldCupGame(worldCupGame)
                .stream()
                .map(GetWorldCupContentsResponse::fromEntity)
                .toList();

    }

    @Transactional
    public long createMyWorldCup(CreateWorldCupRequest request, Long memberId) {

        if (existsGameTitle(request)) {
            throw new DuplicatedWorldCupGameTitleExceptionMember(request.title());
        }

        WorldCupGame newGame = request.toEntity(memberId);

        WorldCupGame savedGame = worldCupGameRepository.save(newGame);

        return savedGame.getId();
    }

    @Transactional
    public void putMyWorldCup(CreateWorldCupRequest request, Long worldCupId, Long memberId) {

        if (existsGameTitle(request)) {
            throw new DuplicatedWorldCupGameTitleExceptionMember(request.title());
        }

        WorldCupGame worldCupGame = worldCupGameRepository
                .findById(worldCupId)
                .orElseThrow(() -> new NotFoundWorldCupGameExceptionMember(worldCupId));

        if (!isGameOwner(memberId, worldCupGame)) {
            throw new NotOwnerGameExceptionMember();
        }

        worldCupGame.simpleUpdate(
                request.title(),
                request.description(),
                request.visibleType()
        );

    }

    // 해당 `WorldCupGame`의 작성자인가?
    private boolean isGameOwner(Long memberId, WorldCupGame worldCupGame) {

        return worldCupGame.getMemberId() == memberId;

    }

    // 이미 존재하는 `WorldCupGame Title`인가?
    private boolean existsGameTitle(CreateWorldCupRequest request) {

        return worldCupGameRepository.existsByTitle(request.title());

    }

    @Transactional
    public void createMyWorldCupContents(CreateWorldCupContentsRequest request, long WorldCupId, long memberId) {

        WorldCupGame worldCupGame = worldCupGameRepository
                .findById(WorldCupId)
                .orElseThrow(() -> new NotFoundWorldCupGameExceptionMember(WorldCupId));

        if (!worldCupGame.isOwner(memberId)) {
            throw new NotOwnerGameExceptionMember();
        }

        List<WorldCupGameContents> newContentsList = createNewContentsList(request, worldCupGame);
        List<MediaFile> newMediaFiles = newContentsList.stream().map(WorldCupGameContents::getMediaFile).toList();

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

                            String objectKey = randomDataGenerator.generate();

                            fileStorage.putObject(mediaFileRequest.mediaData(), objectKey);

                            String fileSize = getFileSize(mediaFileRequest);

                            MediaFile newMediaFile = mediaFileFactory.createMediaFile(
                                    objectKey,
                                    mediaFileRequest.originalName(),
                                    mediaFileRequest.videoPlayDuration(),
                                    mediaFileRequest.videoStartTime(),
                                    mediaFileRequest.fileType(),
                                    mediaFileRequest.detailFileType(),
                                    fileSize
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

    /**
     * 사용자 요청에 포함된 파일의 사이즈를 계산합니다.
     *
     * @param mediaFileRequest 사용자 요청 객체
     * @return MB단위로 계산하여 정수 부분만 반환합니다.
     */
    @NotNull
    private String getFileSize(CreateWorldCupContentsRequest.CreateMediaFileRequest mediaFileRequest) {

        String fileSize = null;

        if (mediaFileRequest.fileType().equals(FileType.STATIC_MEDIA_FILE)) {
            String imageData = mediaFileRequest.mediaData().split(",")[1];
            byte[] binaryData = Base64.getDecoder().decode(imageData);
            int bytes = binaryData.length;
            Double megabytes = bytes / (1024.0 * 1024.0);
            fileSize = String.valueOf(megabytes.intValue());

        } else {
            fileSize = "0";

        }

        return fileSize;
    }

    public List<GetMyWorldCupResponse> getMyWorldCupList(Long memberId) {

        return worldCupGameRepository.findAllByMemberId(memberId).stream()
                .map(GetMyWorldCupResponse::fromEntity)
                .toList();

    }

    public GetWorldCupResponse getMyWorldCup(long worldCupId, Long memberId) {

        WorldCupGame worldCupGame = worldCupGameRepository.findById(worldCupId)
                .orElseThrow(() -> new NotFoundWorldCupGameExceptionMember(worldCupId));

        if (!worldCupGame.isOwner(memberId)) {
            throw new NotOwnerGameExceptionMember();
        }

        return GetWorldCupResponse.fromEntity(worldCupGame);

    }

    public List<GetMyWorldCupContentsResponse> getMyWorldCupContentsList(Long worldCupId, Long memberId) {

        WorldCupGame worldCupGame = worldCupGameRepository.findById(worldCupId)
                .orElseThrow(() -> new NotFoundWorldCupGameExceptionMember(worldCupId));

        if (!worldCupGame.isOwner(memberId)) {
            throw new NotOwnerGameExceptionMember();
        }

        return worldCupGameContentsRepository.findAllByWorldCupGame(worldCupGame)
                .stream()
                .map(GetMyWorldCupContentsResponse::fromEntity)
                .toList();
    }

    @Transactional
    public long updateMyWorldCupContents(UpdateWorldCupContentsRequest request, Long worldCupId,
                                         Long worldCupContentsId, Long memberId) {

        WorldCupGame worldCupGame = worldCupGameRepository.findById(worldCupId)
                .orElseThrow(() -> new NotFoundWorldCupGameExceptionMember(worldCupId));

        if (!worldCupGame.isOwner(memberId)) {
            throw new NotOwnerGameExceptionMember();
        }

        WorldCupGameContents contents = worldCupGameContentsRepository.findById(worldCupContentsId)
                .orElseThrow(() -> new NotFoundWorldCupContentsExceptionMember(worldCupContentsId));

        String objectKey = randomDataGenerator.generate();

        fileStorage.putObject(request.mediaData(), objectKey);

        contents.updateByCommonManage(
                request.contentsName(),
                request.originalName(),
                request.videoStartTime(),
                request.videoPlayDuration(),
                request.visibleType(),
                request.detailFileType(),
                objectKey
        );

        return contents.getId();
    }

    @Transactional
    public long deleteMyWorldCupContents(long worldCupId, long worldCupContentsId, Long memberId) {

        WorldCupGame worldCupGame = worldCupGameRepository.findById(worldCupId)
                .orElseThrow(() -> new NotFoundWorldCupGameExceptionMember(worldCupId));

        if (!worldCupGame.isOwner(memberId)) {
            throw new NotOwnerGameExceptionMember();
        }

        WorldCupGameContents contents = worldCupGameContentsRepository.findById(worldCupContentsId)
                .orElseThrow(() -> new NotFoundWorldCupContentsExceptionMember(worldCupContentsId));

        contents.softDelete();

        return contents.getId();
    }

    @Transactional
    public long deleteMyWorldCup(long worldCupId, Long memberId) {

        WorldCupGame worldCupGame = worldCupGameRepository.findById(worldCupId)
                .orElseThrow(() -> new NotFoundWorldCupGameExceptionMember(worldCupId));

        if (!worldCupGame.isOwner(memberId)) {
            throw new NotOwnerGameExceptionMember();
        }

        worldCupGame.softDelete();

        return worldCupGame.getId();

    }

}
