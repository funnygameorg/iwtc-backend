package com.masikga.itwc.domain.worldcup.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.masikga.itwc.common.util.RandomDataGeneratorInterface;
import com.masikga.itwc.domain.etc.component.MediaFileFactory;
import com.masikga.itwc.domain.etc.model.MediaFile;
import com.masikga.itwc.domain.etc.repository.MediaFileRepository;
import com.masikga.itwc.domain.worldcup.controller.request.CreateWorldCupContentsRequest;
import com.masikga.itwc.domain.worldcup.controller.request.CreateWorldCupRequest;
import com.masikga.itwc.domain.worldcup.controller.request.UpdateWorldCupContentsRequest;
import com.masikga.itwc.domain.worldcup.controller.response.GetMyWorldCupContentsResponse;
import com.masikga.itwc.domain.worldcup.controller.response.GetMyWorldCupResponse;
import com.masikga.itwc.domain.worldcup.controller.response.GetWorldCupContentsResponse;
import com.masikga.itwc.domain.worldcup.controller.response.GetWorldCupResponse;
import com.masikga.itwc.domain.worldcup.exception.DuplicatedWorldCupGameTitleException;
import com.masikga.itwc.domain.worldcup.exception.NotFoundWorldCupContentsException;
import com.masikga.itwc.domain.worldcup.exception.NotFoundWorldCupGameException;
import com.masikga.itwc.domain.worldcup.exception.NotOwnerGameException;
import com.masikga.itwc.domain.worldcup.model.WorldCupGame;
import com.masikga.itwc.domain.worldcup.model.WorldCupGameContents;
import com.masikga.itwc.domain.worldcup.repository.WorldCupGameContentsRepository;
import com.masikga.itwc.domain.worldcup.repository.WorldCupGameRepository;
import com.masikga.itwc.infra.filestorage.FileStorage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
	private final FileStorage s3Component;

	public List<GetWorldCupContentsResponse> getMyWorldCupGameContents(long worldCupId, long memberId) {

		WorldCupGame worldCupGame = worldCupGameRepository
			.findById(worldCupId)
			.orElseThrow(() ->
				new NotFoundWorldCupGameException("%s 는 존재하지 않는 게임입니다.".formatted(worldCupId))
			);

		if (!worldCupGame.isOwner(memberId)) {
			throw new NotOwnerGameException();
		}

		return worldCupGameContentsRepository.findAllByWorldCupGame(worldCupGame)
			.stream()
			.map(GetWorldCupContentsResponse::fromEntity)
			.toList();

	}

	@Transactional
	public long createMyWorldCup(CreateWorldCupRequest request, Long memberId) {

		if (existsGameTitle(request)) {
			throw new DuplicatedWorldCupGameTitleException(request.title());
		}

		WorldCupGame newGame = request.toEntity(memberId);

		WorldCupGame savedGame = worldCupGameRepository.save(newGame);

		return savedGame.getId();
	}

	@Transactional
	public void putMyWorldCup(CreateWorldCupRequest request, Long worldCupId, Long memberId) {

		if (existsGameTitle(request)) {
			throw new DuplicatedWorldCupGameTitleException(request.title());
		}

		WorldCupGame worldCupGame = worldCupGameRepository
			.findById(worldCupId)
			.orElseThrow(() -> new NotFoundWorldCupGameException(worldCupId));

		if (!isGameOwner(memberId, worldCupGame)) {
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
			.orElseThrow(() -> new NotFoundWorldCupGameException(WorldCupId));

		if (!worldCupGame.isOwner(memberId)) {
			throw new NotOwnerGameException();
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

					s3Component.putObject(mediaFileRequest.mediaData(), objectKey);

					MediaFile newMediaFile = mediaFileFactory.createMediaFile(
						objectKey,
						mediaFileRequest.originalName(),
						mediaFileRequest.videoPlayDuration(),
						mediaFileRequest.videoStartTime(),
						mediaFileRequest.fileType(),
						mediaFileRequest.detailFileType()
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

	public List<GetMyWorldCupResponse> getMyWorldCupList(Long memberId) {

		return worldCupGameRepository.findAllByMemberId(memberId).stream()
			.map(GetMyWorldCupResponse::fromEntity)
			.toList();

	}

	public GetWorldCupResponse getMyWorldCup(long worldCupId, Long memberId) {

		WorldCupGame worldCupGame = worldCupGameRepository.findById(worldCupId)
			.orElseThrow(() -> new NotFoundWorldCupGameException(worldCupId));

		if (!worldCupGame.isOwner(memberId)) {
			throw new NotOwnerGameException();
		}

		return GetWorldCupResponse.fromEntity(worldCupGame);

	}

	public List<GetMyWorldCupContentsResponse> getMyWorldCupContentsList(Long worldCupId, Long memberId) {

		WorldCupGame worldCupGame = worldCupGameRepository.findById(worldCupId)
			.orElseThrow(() -> new NotFoundWorldCupGameException(worldCupId));

		if (!worldCupGame.isOwner(memberId)) {
			throw new NotOwnerGameException();
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
			.orElseThrow(() -> new NotFoundWorldCupGameException(worldCupId));

		if (!worldCupGame.isOwner(memberId)) {
			throw new NotOwnerGameException();
		}

		WorldCupGameContents contents = worldCupGameContentsRepository.findById(worldCupContentsId)
			.orElseThrow(() -> new NotFoundWorldCupContentsException(worldCupContentsId));

		String objectKey = randomDataGenerator.generate();

		s3Component.putObject(request.mediaData(), objectKey);

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
			.orElseThrow(() -> new NotFoundWorldCupGameException(worldCupId));

		if (!worldCupGame.isOwner(memberId)) {
			throw new NotOwnerGameException();
		}

		WorldCupGameContents contents = worldCupGameContentsRepository.findById(worldCupContentsId)
			.orElseThrow(() -> new NotFoundWorldCupContentsException(worldCupContentsId));

		contents.softDelete();

		return contents.getId();
	}

	@Transactional
	public long deleteMyWorldCup(long worldCupId, Long memberId) {

		WorldCupGame worldCupGame = worldCupGameRepository.findById(worldCupId)
			.orElseThrow(() -> new NotFoundWorldCupGameException(worldCupId));

		if (!worldCupGame.isOwner(memberId)) {
			throw new NotOwnerGameException();
		}

		worldCupGame.softDelete();

		return worldCupGame.getId();

	}

}
