package com.masikga.member.controller.request;

import com.masikga.member.controller.validator.VerifyMemberNickname;
import com.masikga.member.controller.validator.VerifyMemberPassword;
import com.masikga.member.controller.validator.VerifyMemberServiceId;
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