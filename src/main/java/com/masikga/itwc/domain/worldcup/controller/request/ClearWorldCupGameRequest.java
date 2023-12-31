package com.masikga.itwc.domain.worldcup.controller.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record ClearWorldCupGameRequest(
	@Schema(description = "1위 컨텐츠 id")
	int firstWinnerContentsId,
	@Schema(description = "2위 컨텐츠 id")
	int secondWinnerContentsId,
	@Schema(description = "3, 4위 컨텐츠 id")
	int thirdWinnerContentsId,
	@Schema(description = "3, 4위 컨텐츠 id")
	int fourthWinnerContentsId
) {

	// 요청에 포함된 순위권 이상형을 List로 반환한다.
	@JsonIgnore
	public List<Long> getWinnerIds() {
		return List.of(
			(long)firstWinnerContentsId,
			(long)secondWinnerContentsId,
			(long)thirdWinnerContentsId,
			(long)fourthWinnerContentsId
		);
	}
}