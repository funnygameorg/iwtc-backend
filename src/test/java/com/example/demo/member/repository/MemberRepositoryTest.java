package com.example.demo.member.repository;

import com.example.demo.helper.DataBaseCleanUp;
import com.example.demo.domain.member.model.Member;
import com.example.demo.domain.member.repository.MemberRepository;
import com.example.demo.helper.testbase.IntegrationBaseTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static com.example.demo.helper.TestConstant.SUCCESS_PREFIX;

public class MemberRepositoryTest implements IntegrationBaseTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private DataBaseCleanUp dataBaseCleanUp;

    @AfterEach
    public void tearDown() {
        dataBaseCleanUp.truncateAllEntity();
    }

    @Nested
    @DisplayName("Member Table의 serviceId 중복 체크를 할 수 있다.")
    public class existsServiceId {
        @Test
        @DisplayName(SUCCESS_PREFIX + " serviceId 존재 O")
        public void success1() {
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
        @DisplayName(SUCCESS_PREFIX + " serviceId 존재 X")
        public void success2() {
            Member member = Member.builder()
                    .serviceId("A")
                    .nickname("A")
                    .password("A")
                    .build();
            memberRepository.save(member);

            Boolean result = memberRepository.existsServiceId("ZZ");

            assert !result;
        }
    }

    @Nested
    @DisplayName("Member Table의 nickname 중복 체크")
    public class existsNickname {

        @Test
        @DisplayName(SUCCESS_PREFIX + " nickname 존재 O")
        public void success1() {
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
        @DisplayName(SUCCESS_PREFIX + "nickname 존재 X")
        public void success2() {
            Member member = Member.builder()
                    .serviceId("A")
                    .nickname("A")
                    .password("A")
                    .build();
            memberRepository.save(member);

            Boolean result = memberRepository.existsNickname("BZ");

            assert !result;
        }

    }

    @Nested
    @DisplayName("Member Table의 serviceId, password를 가진 row가 존재하는지 확인할 수 있다.")
    public class findByMemberIdByServiceIdAndPassword {


        @Test
        @DisplayName(SUCCESS_PREFIX + "Member 존재 O")
        public void success1() {
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
        @DisplayName(SUCCESS_PREFIX + "Member 존재 X")
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

    }


}