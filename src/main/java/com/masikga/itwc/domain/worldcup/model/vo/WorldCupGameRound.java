package com.masikga.itwc.domain.worldcup.model.vo;

import java.util.Arrays;

import com.masikga.itwc.domain.worldcup.exception.NotSupportedGameRoundException;

public enum WorldCupGameRound {
	ROUND_2(2),
	ROUND_4(4),
	ROUND_8(8),
	ROUND_16(16),
	ROUND_32(32),
	ROUND_64(64),
	ROUND_128(128),
	ROUND_256(256);

	public final int roundValue;

	WorldCupGameRound(int roundValue) {
		this.roundValue = roundValue;
	}

	// 게임에서 진행 가능한 라운드 여부 체크
	public boolean isAvailableRound(Long contentsSize) {
		return this.roundValue <= contentsSize;
	}

	// 해당 라운드의 게임 컨텐츠 조회 단위 나누기
	public int getGameContentsSizePerRequest(int divide) {
		return this.roundValue / divide;
	}

	public static WorldCupGameRound getRoundFromValue(int value) {
		return Arrays.stream(WorldCupGameRound.values())
			.filter(round -> round.roundValue == value)
			.findFirst()
			.orElseThrow(() -> new NotSupportedGameRoundException(value + "는 해당 게임에서 플레이 불가 라운드입니다."));
	}
}
