package com.masikga.worldcupgame.domain.worldcup.controller.request;

import com.masikga.worldcupgame.domain.worldcup.controller.validator.VerifyVisibleType;
import com.masikga.worldcupgame.domain.worldcup.controller.validator.worldcup.VerifyWorldCupDescription;
import com.masikga.worldcupgame.domain.worldcup.controller.validator.worldcup.VerifyWorldCupTitle;
import com.masikga.worldcupgame.domain.worldcup.model.WorldCupGame;
import com.masikga.worldcupgame.domain.worldcup.model.vo.VisibleType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema
@Builder
public record CreateWorldCupRequest(
	@VerifyWorldCupTitle
	String title,

	@VerifyWorldCupDescription
	String description,

	@VerifyVisibleType
	VisibleType visibleType

) {

	public WorldCupGame toEntity(long memberId) {
		return WorldCupGame.createNewGame(
			title,
			description,
			visibleType,
			memberId
		);
	}
}