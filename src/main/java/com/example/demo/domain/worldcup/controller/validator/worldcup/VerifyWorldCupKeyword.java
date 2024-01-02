package com.example.demo.domain.worldcup.controller.validator.worldcup;

import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Retention;
import java.util.Objects;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

@Retention(RUNTIME)
@Schema(description = "월드컵 키워드 [최소: 1, 최대: 10]")
@Constraint(validatedBy = VerifyWorldCupKeyword.Validator.class)
public @interface VerifyWorldCupKeyword {

	String message() default "월드컵 키워드 : 최소: 1, 최대 10";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	class Validator implements ConstraintValidator<VerifyWorldCupKeyword, String> {

		@Override
		public boolean isValid(String value, ConstraintValidatorContext context) {
			if (Objects.nonNull(value)) {
				return value.length() >= 1 && value.length() <= 10;
			}
			return true;
		}

	}
}
