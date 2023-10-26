package com.example.demo.member.repository;

import com.example.demo.common.DataBaseCleanUp;
import com.example.demo.domain.member.model.Member;
import com.example.demo.domain.member.model.repository.MemberRepository;
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
    @DisplayName("사용자의 서비스 아이디가 있다면 true를 반환한다.")
    public void Return1_If_ExistsServiceId() {
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
    @DisplayName("사용자의 서비스 아이디가 없다면 false을 반환한다.")
    public void Return0_If_NotExistsServiceId() {
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
    @DisplayName("사용자의 서비스 아이디가 있다면 true를 반환한다.")
    public void Return1_If_ExistsNickname() {
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
    @DisplayName("사용자의 서비스 아이디가 없다면 false를 반환한다.")
    public void Return0_If_NotExistsNickname() {
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
    @DisplayName("서비스 아이디와 패스워드에 일치하는 멤버 아이디가 있다.")
    public void findByMemberIdByServiceIdAndPassword_성공() {
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
    @DisplayName("서비스 아이디와 패스워드에 일치하는 멤버 아이디가 없다.")
    public void findByMemberIdByServiceIdAndPassword_실패() {
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