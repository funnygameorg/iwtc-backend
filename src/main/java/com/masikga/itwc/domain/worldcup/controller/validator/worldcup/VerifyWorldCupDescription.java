package com.masikga.itwc.domain.worldcup.controller.validator.worldcup;

import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Retention;
import java.util.Objects;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

@Retention(RUNTIME)
@Schema(description = "월드컵 내용 [최대 : 100]", maxLength = 100)
@Constraint(validatedBy = VerifyWorldCupDescription.Validator.class)
public @interface VerifyWorldCupDescription {

	String message() default "월드컵 내용 : 최대 100";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	class Validator implements ConstraintValidator<VerifyWorldCupDescription, String> {

		@Override
		public boolean isValid(String value, ConstraintValidatorContext context) {

			if (Objects.nonNull(value)) {
				return value.length() <= 100;
			}
			return true;
		}

	}
}
