package com.masikga.itwc.domain.member.controller.request;

import com.masikga.itwc.domain.member.controller.validator.VerifyMemberNickname;
import com.masikga.itwc.domain.member.controller.validator.VerifyMemberPassword;
import com.masikga.itwc.domain.member.controller.validator.VerifyMemberServiceId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema
@Builder
public record SignUpRequest(
	@VerifyMemberServiceId
	String serviceId,

	@VerifyMemberNickname
	String nickname,

	@VerifyMemberPassword
	String password
) {

}