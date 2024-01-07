package com.masikga.itwc.domain.worldcup.controller.request;

import com.masikga.itwc.domain.worldcup.controller.validator.worldcupcontents.VerifyWorldCupContentsName;
import com.masikga.itwc.domain.worldcup.model.vo.VisibleType;

public record UpdateWorldCupContentsRequest(

	@VerifyWorldCupContentsName
	String contentsName,
	String originalName,
	String mediaData,
	String videoStartTime,
	Integer videoPlayDuration,
	VisibleType visibleType
) {
}
