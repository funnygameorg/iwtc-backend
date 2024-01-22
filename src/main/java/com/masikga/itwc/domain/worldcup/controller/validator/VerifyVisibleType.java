package com.masikga.itwc.domain.worldcup.controller.validator;

import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Retention;

import com.masikga.itwc.domain.worldcup.model.vo.VisibleType;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

@Retention(RUNTIME)
@Schema(description = "공개 여부")
@Constraint(validatedBy = VerifyVisibleType.Validator.class)
public @interface VerifyVisibleType {

	String message() default "공개 여부: 필수 값";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	class Validator implements ConstraintValidator<VerifyVisibleType, VisibleType> {

		@Override
		public boolean isValid(VisibleType value, ConstraintValidatorContext context) {
			return value != null;
		}

	}

}
