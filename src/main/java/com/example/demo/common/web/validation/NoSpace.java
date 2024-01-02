package com.example.demo.common.web.validation;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Retention(RUNTIME)
@Target({FIELD, PARAMETER, ANNOTATION_TYPE})
@Constraint(validatedBy = NoSpacesBetweenValidator.class)
public @interface NoSpace {
	String message() default "공백이 허용되지 않습니다.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}

