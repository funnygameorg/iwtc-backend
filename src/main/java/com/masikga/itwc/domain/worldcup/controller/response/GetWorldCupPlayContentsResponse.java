package com.masikga.itwc.domain.worldcup.controller.response;

import static java.util.stream.Collectors.*;

import java.util.List;

import com.masikga.itwc.domain.worldcup.repository.projection.GetDividedWorldCupGameContentsProjection;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

public record GetWorldCupPlayContentsResponse(
	@Schema(description = "이상형 월드컵 ID")
	Long worldCupId,
	@Schema(description = "이상형 월드컵 타이틀")
	String title,
	@Schema(description = "현재 진행중인 라운드")
	Integer round,
	@Schema(description = "플레이하기 위한 이상형 컨텐츠 리스트")
	List<PlayContents> contentsList
) {
	public static GetWorldCupPlayContentsResponse fromProjection(
		Long worldCupGameId,
		String WorldCupGameTitle,
		int worldCupGameRound,
		List<GetDividedWorldCupGameContentsProjection> gameContentsProjections
	) {
		return new GetWorldCupPlayContentsResponse(
			worldCupGameId,
			WorldCupGameTitle,
			worldCupGameRound,
			gameContentsProjections
				.stream()
				.map(PlayContents::fromProjection)
				.collect(toList())
		);
	}

	@Getter
	public static class PlayContents {
		@Schema(description = "미디어 컨텐츠 파일의 타입 (현재 지원 형식 : STATIC_MEDIA_FILE, INTERNET_VIDEO_URL)")
		String fileType;
		@Schema(description = "이상형 컨텐츠 ID")
		Long contentsId;
		@Schema(description = "이상형 컨텐츠 이름")
		String name;
		@Schema(description = "미디어 파일 ID")
		Long mediaFileId;
		@Schema(description = "INTERNET_VIDEO_URL 타입인 경우 재생 시작 시간 포맷 : '00000'(모두 숫자 표현) ")
		String internetMovieStartPlayTime;
		@Schema(description = "반복 시간(초)")
		Integer playDuration;

		public static PlayContents fromProjection(GetDividedWorldCupGameContentsProjection projection) {
			PlayContents instance = new PlayContents();
			instance.fileType = projection.FileType();
			instance.contentsId = projection.contentsId();
			instance.name = projection.name();
			instance.mediaFileId = projection.mediaFileId();
			instance.internetMovieStartPlayTime = projection.movieStartTime();
			instance.playDuration = projection.moviePlayDuration();
			return instance;
		}

	}
}
