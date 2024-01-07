package com.masikga.itwc.domain.worldcup.controller.validator.worldcupcontents;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "월드컵 컨텐츠 이름 [필수 값]")
@NotBlank(message = "월드컵 컨텐츠 이름: 필수 값")
public @interface VerifyWorldCupContentsName {

}
