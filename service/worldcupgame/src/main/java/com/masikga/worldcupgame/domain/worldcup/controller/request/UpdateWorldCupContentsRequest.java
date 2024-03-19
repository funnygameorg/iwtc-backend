package com.masikga.worldcupgame.domain.worldcup.controller.request;

import com.masikga.worldcupgame.domain.worldcup.controller.validator.worldcupcontents.VerifyCreateDetailFileType;
import com.masikga.worldcupgame.domain.worldcup.controller.validator.worldcupcontents.VerifyWorldCupContentsName;
import com.masikga.worldcupgame.domain.worldcup.model.vo.VisibleType;
import lombok.Builder;

@Builder
public record UpdateWorldCupContentsRequest(

	@VerifyWorldCupContentsName
	String contentsName,
	String originalName,
	String mediaData,
	@VerifyCreateDetailFileType
	String detailFileType,
	String videoStartTime,
	Integer videoPlayDuration,
	VisibleType visibleType
) {
}
