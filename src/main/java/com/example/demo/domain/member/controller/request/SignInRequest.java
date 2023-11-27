package com.example.demo.domain.member.controller.request;

import com.example.demo.common.web.validation.NoSpace;
import com.example.demo.domain.member.controller.validator.VerifyMemberPassword;
import com.example.demo.domain.member.controller.validator.VerifyMemberServiceId;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record SignInRequest(

        @VerifyMemberServiceId
        String serviceId,

        @VerifyMemberPassword
        String password
) {}

