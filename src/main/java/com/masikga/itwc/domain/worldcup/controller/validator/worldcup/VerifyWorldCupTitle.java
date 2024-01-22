package com.masikga.itwc.domain.worldcup.controller.validator.worldcup;

import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Retention;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

@Retention(RUNTIME)
@Schema(description = "월드컵 이름")
@Constraint(validatedBy = VerifyWorldCupTitle.Validator.class)
public @interface VerifyWorldCupTitle {

	String message() default "월드컵 이름: 필수";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	class Validator implements ConstraintValidator<VerifyWorldCupTitle, String> {

		@Override
		public boolean isValid(String value, ConstraintValidatorContext context) {
			return value != null && !value.isEmpty();
		}
	}

}
