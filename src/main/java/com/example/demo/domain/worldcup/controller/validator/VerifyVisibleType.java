package com.example.demo.domain.worldcup.controller.validator;

import com.example.demo.domain.worldcup.model.vo.VisibleType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.constraints.NotNull;

@Schema(description = "공개 여부")
@NotNull(message = "공개 여부: 필수 값")
public @interface VerifyVisibleType {

}
