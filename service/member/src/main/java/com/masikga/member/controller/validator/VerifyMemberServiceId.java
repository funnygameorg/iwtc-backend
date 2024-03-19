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
@Schema(description = "서비스에서 사용하는 아이디 [6자리 이상, 20자리 이하]")
@Constraint(validatedBy = VerifyMemberServiceId.Validator.class)
public @interface VerifyMemberServiceId {

    String message() default "사용자 아이디 : 6자리 이상, 20자리 이하";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class Validator implements ConstraintValidator<VerifyMemberServiceId, String> {

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            return Objects.nonNull(value) && value.length() >= 6 && value.length() <= 20;
        }
    }
}
