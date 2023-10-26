package com.example.demo.common.jpa;

import com.example.demo.common.web.memberresolver.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

@RequiredArgsConstructor
public class CustomAuditAware implements AuditorAware<MemberDto> {

    private final MemberDto memberDto;
    @Override
    public Optional<MemberDto> getCurrentAuditor() {
        return Optional.ofNullable(memberDto);
    }
}
