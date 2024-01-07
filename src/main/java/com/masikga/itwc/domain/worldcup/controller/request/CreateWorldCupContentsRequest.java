package com.masikga.itwc.domain.worldcup.controller.request;

import java.util.List;

import com.masikga.itwc.domain.etc.model.vo.FileType;
import com.masikga.itwc.domain.worldcup.controller.validator.VerifyVisibleType;
import com.masikga.itwc.domain.worldcup.controller.validator.worldcupcontents.VerifyWorldCupContentsName;
import com.masikga.itwc.domain.worldcup.model.vo.VisibleType;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
@Schema
public record CreateWorldCupContentsRequest(
	List<CreateContentsRequest> data
) {

	@Builder
	@Schema
	public record CreateContentsRequest(
		@VerifyWorldCupContentsName
		String contentsName,

		@VerifyVisibleType
		VisibleType visibleType,

		CreateMediaFileRequest createMediaFileRequest
	) {
	}

	@Builder
	@Schema
	public record CreateMediaFileRequest(

		FileType fileType,

		@Schema(description = "월드컵 컨텐츠 파일명")
		@NotBlank(message = "월드컵 컨텐츠 파일 이름: 필수 값")
		String mediaData,

		// STATIC FILE TYPE
		String originalName,

		// MOVIE URL TYPE
		String videoStartTime,

		Integer videoPlayDuration

	) {
	}

}
