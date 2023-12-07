package com.example.demo.member.repository;

import com.example.demo.helper.DataBaseCleanUp;
import com.example.demo.domain.member.model.Member;
import com.example.demo.domain.member.repository.MemberRepository;
import com.example.demo.helper.testbase.IntegrationBaseTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static com.example.demo.helper.TestConstant.SUCCESS_PREFIX;
import static org.assertj.core.api.Assertions.*;

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
            
            // when then
            assertThat(memberRepository.existsServiceId(member.getServiceId())).isTrue();
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
            
            // when then
            assertThat(memberRepository.existsServiceId("ZZ")).isFalse();
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

            // when then
            assertThat(memberRepository.existsNickname(member.getNickname())).isTrue();
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

            // when then
            assertThat(memberRepository.existsNickname("BZ")).isFalse();
        }

    }



}