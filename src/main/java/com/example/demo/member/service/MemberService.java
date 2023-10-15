package com.example.demo.member.service;

import com.example.demo.member.controller.dto.SignUpRequest;
import com.example.demo.member.exception.DuplicatedNicknameException;
import com.example.demo.member.exception.DuplicatedServiceIdException;
import com.example.demo.member.model.Member;
import com.example.demo.member.model.MemberRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public void signUp(@Valid SignUpRequest request, LocalDateTime requestDate) {
        Member newMember = Member.signUp(
                request.serviceId(),
                request.nickname(),
                request.password(),
                requestDate
        );

        if(memberRepository.existsNickname(newMember.getNickname()) == 1) {
            throw new DuplicatedNicknameException();
        }
        if(memberRepository.existsServiceId(newMember.getServiceId()) == 1) {
            throw new DuplicatedServiceIdException();
        }

        memberRepository.save(newMember);
    }
}
