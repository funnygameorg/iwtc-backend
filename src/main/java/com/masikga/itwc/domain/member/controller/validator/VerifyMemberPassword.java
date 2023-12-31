package com.masikga.itwc.domain.member.controller.validator;

import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Retention;
import java.util.Objects;

import com.masikga.itwc.common.web.validation.NoSpace;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

@NoSpace
@Retention(RUNTIME)
@Schema(description = "암호 [6자리 이상]")
@Constraint(validatedBy = VerifyMemberPassword.Validator.class)
public @interface VerifyMemberPassword {

	String message() default "사용자 암호 : 6자리 이상";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	class Validator implements ConstraintValidator<VerifyMemberPassword, String> {

		@Override
		public boolean isValid(String value, ConstraintValidatorContext context) {
			return Objects.nonNull(value) && value.length() >= 6;
		}
	}

}
