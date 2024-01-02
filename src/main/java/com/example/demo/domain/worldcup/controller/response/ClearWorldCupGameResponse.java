package com.example.demo.domain.worldcup.controller.response;

import com.example.demo.domain.worldcup.model.WorldCupGameContents;

import io.swagger.v3.oas.annotations.media.Schema;

public record ClearWorldCupGameResponse(

	@Schema(description = "컨텐츠 이름")
	String contentsName,

	@Schema(description = "컨텐츠 아이디")
	Long contentsId,

	@Schema(description = "미디어 파일 아이디")
	Long mediaFileId,

	@Schema(description = "등수 [ 범위 1 ~ 4 ]")
	int rank
) {

	public static ClearWorldCupGameResponse fromEntity(
		WorldCupGameContents contents,
		Integer rank
	) {
		return new ClearWorldCupGameResponse(
			contents.getName(),
			contents.getId(),
			contents.getMediaFile().getId(),
			rank
		);
	}
}