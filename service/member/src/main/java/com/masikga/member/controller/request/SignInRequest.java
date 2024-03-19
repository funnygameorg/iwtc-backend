package com.masikga.member.controller.request;

import com.masikga.member.controller.validator.VerifyMemberPassword;
import com.masikga.member.controller.validator.VerifyMemberServiceId;
import lombok.Builder;

@Builder
public record SignInRequest(

        @VerifyMemberServiceId
        String serviceId,

        @VerifyMemberPassword
        String password
) {
}

