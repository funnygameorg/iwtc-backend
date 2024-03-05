package com.masikga.itwc.common.web.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/*
    "A A A A A", "가 가", "1 1", " " 과 같은 모든 공백을 허용하지 않습니다.
 */
public class NoSpacesBetweenValidator implements ConstraintValidator<NoSpace, String> {
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return value != null && !value.matches(".*\\s+.*");
	}
}
