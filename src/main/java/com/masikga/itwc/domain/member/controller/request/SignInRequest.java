package com.masikga.itwc.domain.member.controller.request;

import com.masikga.itwc.domain.member.controller.validator.VerifyMemberPassword;
import com.masikga.itwc.domain.member.controller.validator.VerifyMemberServiceId;

import lombok.Builder;

@Builder
public record SignInRequest(

	@VerifyMemberServiceId
	String serviceId,

	@VerifyMemberPassword
	String password
) {
}

