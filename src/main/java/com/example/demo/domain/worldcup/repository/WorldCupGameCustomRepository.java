package com.example.demo.domain.worldcup.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.demo.domain.worldcup.repository.projection.GetAvailableGameRoundsProjection;
import com.example.demo.domain.worldcup.repository.projection.GetDividedWorldCupGameContentsProjection;
import com.example.demo.domain.worldcup.repository.projection.GetWorldCupGamePageProjection;

public interface WorldCupGameCustomRepository {

	Page<GetWorldCupGamePageProjection> getWorldCupGamePage(
		LocalDate startDate,
		LocalDate endDate,
		String worldCupGameKeyword,
		Pageable pageable,
		Long memberId
	);

	Boolean existsWorldCupGame(Long worldCupGameId);

	GetAvailableGameRoundsProjection getAvailableGameRounds(Long worldCupGameId);

	// TODO : 인프라 구현체가 자주 변하는 기능
	void incrementWorldCupGameViews(Long worldCupGameId);

	List<GetDividedWorldCupGameContentsProjection> getDividedWorldCupGameContents(
		Long worldCupId,
		int wantedContentsSize,
		List<Long> alreadyPlayedContentsIds
	);
}
