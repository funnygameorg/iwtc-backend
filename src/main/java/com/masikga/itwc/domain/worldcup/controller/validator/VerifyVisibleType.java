package com.masikga.itwc.domain.worldcup.controller.validator;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "공개 여부")
@NotNull(message = "공개 여부: 필수 값")
public @interface VerifyVisibleType {

}
