package com.masikga.itwc.domain.worldcup.service;

import static java.util.Arrays.*;
import static java.util.Comparator.*;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.masikga.itwc.domain.worldcup.controller.request.ClearWorldCupGameRequest;
import com.masikga.itwc.domain.worldcup.controller.response.ClearWorldCupGameResponse;
import com.masikga.itwc.domain.worldcup.controller.response.GetAvailableGameRoundsResponse;
import com.masikga.itwc.domain.worldcup.controller.response.GetGameResultContentsListResponse;
import com.masikga.itwc.domain.worldcup.controller.response.GetWorldCupPlayContentsResponse;
import com.masikga.itwc.domain.worldcup.exception.IllegalWorldCupGameContentsException;
import com.masikga.itwc.domain.worldcup.exception.NoRoundsAvailableToPlayException;
import com.masikga.itwc.domain.worldcup.exception.NotFoundWorldCupContentsException;
import com.masikga.itwc.domain.worldcup.exception.NotFoundWorldCupGameException;
import com.masikga.itwc.domain.worldcup.model.WorldCupGame;
import com.masikga.itwc.domain.worldcup.model.WorldCupGameContents;
import com.masikga.itwc.domain.worldcup.model.vo.WorldCupGameRound;
import com.masikga.itwc.domain.worldcup.repository.WorldCupGameContentsRepository;
import com.masikga.itwc.domain.worldcup.repository.WorldCupGameRepository;
import com.masikga.itwc.domain.worldcup.repository.projection.GetAvailableGameRoundsProjection;
import com.masikga.itwc.domain.worldcup.repository.projection.GetDividedWorldCupGameContentsProjection;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WorldCupGameContentsService {

	private final WorldCupGameRepository worldCupGameRepository;
	private final WorldCupGameContentsRepository worldCupGameContentsRepository;

	@Transactional
	public GetAvailableGameRoundsResponse getAvailableGameRounds(Long worldCupGameId) {

		if (!worldCupGameRepository.existsWorldCupGame(worldCupGameId)) {

			throw new NotFoundWorldCupGameException(worldCupGameId);

		}

		GetAvailableGameRoundsProjection result = worldCupGameRepository.getAvailableGameRounds(worldCupGameId);
		List<Integer> availableGameRounds = generateAvailableRounds(result.totalContentsSize());

		if (availableGameRounds.size() == 0) {

			throw new NoRoundsAvailableToPlayException(result.toString());

		}

		try {
			worldCupGameRepository.incrementWorldCupGameViews(worldCupGameId);
		} catch (Exception e) {
			log.warn("Failed increment View! game id : {}", worldCupGameId);
		}

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

	/**
	 * 월드컵 게임에 포함된 컨텐츠 리스트를 반환한다. (게임 플레이에 사용)
	 *
	 * @param worldCupGameId           플레이하는 월드컵 게임
	 * @param currentRound             현재 라운드
	 * @param sliceContents            몇 분의 일의 컨텐츠 양을 응답할 것인가?
	 * @param alreadyPlayedContentsIds 사용자가 이미 플레이한 컨텐츠 아이디 리스트
	 * @return 사용자가 플레이할 월드컵 컨텐츠 리스트
	 */
	public GetWorldCupPlayContentsResponse getPlayContents(
		Long worldCupGameId,
		int currentRound,
		int sliceContents,
		List<Long> alreadyPlayedContentsIds
	) {

		WorldCupGame worldCupGame = worldCupGameRepository
			.findById(worldCupGameId)
			.orElseThrow(() -> new NotFoundWorldCupGameException(worldCupGameId));

		WorldCupGameRound worldCupGameRound = WorldCupGameRound.getRoundFromValue(currentRound);

		int wantedContentsSize = worldCupGameRound.getGameContentsSizePerRequest(sliceContents);

		List<GetDividedWorldCupGameContentsProjection> contentsProjections = worldCupGameRepository
			.getDividedWorldCupGameContents(
				worldCupGameId,
				wantedContentsSize,
				alreadyPlayedContentsIds
			);

		if (equalsExpectedContentsSize(wantedContentsSize, contentsProjections.size())) {
			throw new IllegalWorldCupGameContentsException(
				"조회 컨텐츠 수가 다름 %s, %s".formatted(wantedContentsSize, contentsProjections.size())
			);
		}

		if (containsAlreadyContents(alreadyPlayedContentsIds, contentsProjections)) {
			throw new IllegalWorldCupGameContentsException("컨텐츠 중복 : 이미 플레이한 컨텐츠 %s, 조회 성공 컨텐츠 %s"
				.formatted(alreadyPlayedContentsIds, contentsProjections)
			);
		}

		var notShuffledContentsIds = getContentsIds(contentsProjections);

		while (notShuffledContentsIds.equals(getContentsIds(contentsProjections))) {
			Collections.shuffle(contentsProjections);
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

	// 컨텐츠 리스트 결과에서 아이디만 반환한다.
	public List<Long> getContentsIds(List<GetDividedWorldCupGameContentsProjection> contentsList) {
		return contentsList.stream().map(GetDividedWorldCupGameContentsProjection::contentsId).toList();
	}

	@Transactional
	public List<ClearWorldCupGameResponse> clearWorldCupGame(long worldCupId, ClearWorldCupGameRequest request) {

		var contents = worldCupGameContentsRepository.findAllById(request.getWinnerIds());

		if (contents.size() != 4) {
			throw new NotFoundWorldCupContentsException("조회 실패 개수 " + (4 - contents.size()));
		}

		try {

			worldCupGameContentsRepository.saveWinnerContentsScore(worldCupId, request.firstWinnerContentsId(), 10);
			worldCupGameContentsRepository.saveWinnerContentsScore(worldCupId, request.secondWinnerContentsId(), 7);
			worldCupGameContentsRepository.saveWinnerContentsScore(worldCupId, request.thirdWinnerContentsId(), 4);
			worldCupGameContentsRepository.saveWinnerContentsScore(worldCupId, request.fourthWinnerContentsId(), 4);

		} catch (Exception e) {
			log.warn("Failed increment Contents Score! contents id : {}", request);
		}

		return getClearWorldCupGameResponses(request, contents);

	}

	// 조회한 컨텐츠들을 랭크별로 정렬한다.
	// TODO : `request`의 순위 객체를 배열에 넣고 코드 작성하기
	private List<ClearWorldCupGameResponse> getClearWorldCupGameResponses(ClearWorldCupGameRequest request,
		List<WorldCupGameContents> contents) {

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
			.sorted(comparing(WorldCupGameContents::getGameScore, reverseOrder()))
			.map(GetGameResultContentsListResponse::fromEntity)
			.toList();

	}

}
