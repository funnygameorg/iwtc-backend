package com.masikga.itwc.domain.worldcup.controller.validator.worldcupcontents;

import com.masikga.itwc.common.web.validation.NoSpace;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.util.List;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@NoSpace
@Retention(RUNTIME)
@Schema(description = "허용 미디어 파일 형식 : GIF, PNG, JPEG, JPG, YOU_TUBE_URL")
@Constraint(validatedBy = VerifyCreateDetailFileType.Validator.class)
public @interface VerifyCreateDetailFileType {

	String message() default "허용 미디어 파일 형식 : GIF, PNG, JPEG, JPG, YOU_TUBE_URL";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	class Validator implements ConstraintValidator<VerifyCreateDetailFileType, String> {

		@Override
		public boolean isValid(String value, ConstraintValidatorContext context) {
			return value != null && List.of("GIF", "PNG", "JPEG", "JPG", "YOU_TUBE_URL").contains(value);
		}
	}

}
