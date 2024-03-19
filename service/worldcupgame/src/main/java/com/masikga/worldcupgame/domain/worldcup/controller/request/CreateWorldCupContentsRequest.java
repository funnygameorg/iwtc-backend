package com.masikga.worldcupgame.domain.worldcup.controller.request;

import com.masikga.worldcupgame.domain.etc.model.vo.FileType;
import com.masikga.worldcupgame.domain.worldcup.controller.validator.VerifyVisibleType;
import com.masikga.worldcupgame.domain.worldcup.controller.validator.worldcupcontents.VerifyCreateDetailFileType;
import com.masikga.worldcupgame.domain.worldcup.controller.validator.worldcupcontents.VerifyWorldCupContentsName;
import com.masikga.worldcupgame.domain.worldcup.model.vo.VisibleType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.util.List;

@Builder
@Schema
public record CreateWorldCupContentsRequest(
	@Valid
	List<CreateContentsRequest> data
) {

	@Builder
	@Schema
	public record CreateContentsRequest(
		@VerifyWorldCupContentsName
		String contentsName,

		@VerifyVisibleType
		VisibleType visibleType,

		@Valid
		CreateMediaFileRequest createMediaFileRequest
	) {
	}

	@Builder
	@Schema
	public record CreateMediaFileRequest(

		FileType fileType,

		@VerifyCreateDetailFileType
		String detailFileType,

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
