package com.masikga.worldcupgame.domain.worldcup.controller.validator.worldcupcontents;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Schema(description = "월드컵 컨텐츠 이름 [필수 값]")
@Constraint(validatedBy = VerifyWorldCupContentsName.Validator.class)
public @interface VerifyWorldCupContentsName {

    String message() default "월드컵 컨텐츠 이름: 필수";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class Validator implements ConstraintValidator<VerifyWorldCupContentsName, String> {

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            return value != null && !value.isEmpty();
        }
    }

}
