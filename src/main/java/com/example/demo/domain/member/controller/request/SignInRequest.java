package com.example.demo.domain.member.controller.request;

import com.example.demo.domain.member.controller.validator.VerifyMemberPassword;
import com.example.demo.domain.member.controller.validator.VerifyMemberServiceId;

import lombok.Builder;

@Builder
public record SignInRequest(

	@VerifyMemberServiceId
	String serviceId,

	@VerifyMemberPassword
	String password
) {
}

