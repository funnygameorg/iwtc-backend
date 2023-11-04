package com.example.demo.member.repository;

import com.example.demo.helper.DataBaseCleanUp;
import com.example.demo.domain.member.model.Member;
import com.example.demo.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

@ActiveProfiles("test")
@SpringBootTest
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private DataBaseCleanUp dataBaseCleanUp;

    @AfterEach
    public void tearDown() {
        dataBaseCleanUp.truncateAllEntity();
    }

    @Test
    @DisplayName("사용자 serviceId 중복 체크 - 사용자의 서비스 아이디가 있음")
    public void existsServiceId1() {
        Member member = Member.builder()
                .serviceId("A")
                .nickname("A")
                .password("A")
                .build();
        memberRepository.save(member);

        Boolean result = memberRepository.existsServiceId(member.getServiceId());

        assert result;
    }

    @Test
    @DisplayName("사용자 serviceId 중복 체크 - 사용자의 서비스 아이디가 없음")
    public void existsServiceId2() {
        Member member = Member.builder()
                .serviceId("A")
                .nickname("A")
                .password("A")
                .build();
        memberRepository.save(member);

        Boolean result = memberRepository.existsServiceId("ZZ");

        assert !result;
    }

    @Test
    @DisplayName("사용자 nickname 중복 체크 - 사용자의 닉네임이 있음")
    public void existsNickname1() {
        Member member = Member.builder()
                .serviceId("A")
                .nickname("A")
                .password("A")
                .build();
        memberRepository.save(member);

        Boolean result = memberRepository.existsNickname(member.getNickname());

        assert result;
    }

    @Test
    @DisplayName("사용자 nickname 중복 체크 - 사용자의 서비스 아이디가 없음")
    public void existsNickname2() {
        Member member = Member.builder()
                .serviceId("A")
                .nickname("A")
                .password("A")
                .build();
        memberRepository.save(member);

        Boolean result = memberRepository.existsNickname("BZ");

        assert !result;
    }

    @Test
    @DisplayName("서비스 serviceId, password 중복 체크 - 해당하는 사용자가 존재")
    public void findByMemberIdByServiceIdAndPassword1() {
        Member member = Member.builder()
                .serviceId("A")
                .nickname("A")
                .password("B")
                .build();
        memberRepository.save(member);
        Optional<Long> result = memberRepository.findByMemberIdByServiceIdAndPassword("A", "B");

        assert result.isPresent();
    }

    @Test
    @DisplayName("서비스 serviceId, password 중복 체크 - 해당하는 사용자가 존재하지 않음")
    public void findByMemberIdByServiceIdAndPassword2() {
        Member member = Member.builder()
                .serviceId("A")
                .nickname("A")
                .password("C")
                .build();
        memberRepository.save(member);

        Optional<Long> result = memberRepository.findByMemberIdByServiceIdAndPassword("A", "B");

        assert result.isEmpty();
    }

    public void memeber_저장_성공() {

    }

}