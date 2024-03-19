package com.masikga.member.controller.validator;

import com.masikga.member.common.web.validation.NoSpace;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.util.Objects;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@NoSpace
@Retention(RUNTIME)
@Schema(description = "사용자 닉네임 [2자리 이상, 10자리 이하]")
@Constraint(validatedBy = VerifyMemberNickname.Validator.class)
public @interface VerifyMemberNickname {

    String message() default "사용자 닉네임 : 2자리 이상, 10자리 이하";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class Validator implements ConstraintValidator<VerifyMemberNickname, String> {

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {

            return Objects.nonNull(value) && value.length() >= 2 && value.length() <= 10;

        }
    }

}
