package com.example.demo.member.repository;

import com.example.demo.member.model.Member;
import com.example.demo.member.model.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@ActiveProfiles("local")
@SpringBootTest
@Transactional
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository sut;

    @Test
    @DisplayName("사용자의 서비스 아이디가 있다면 true를 반환한다.")
    public void Return1_If_ExistsServiceId() {
        LocalDateTime signUpDate = LocalDateTime.of(2022, 10, 1, 1, 1, 1);
        Member member = Member.builder()
                .serviceId("A")
                .nickname("A")
                .password("A")
                .signUpdate(signUpDate)
                .build();
        sut.save(member);

        Boolean result = sut.existsServiceId(member.getServiceId());

        assert result;
    }

    @Test
    @DisplayName("사용자의 서비스 아이디가 없다면 false을 반환한다.")
    public void Return0_If_NotExistsServiceId() {
        LocalDateTime signUpDate = LocalDateTime.of(2022, 10, 1, 1, 1, 1);
        Member member = Member.builder()
                .serviceId("A")
                .nickname("A")
                .password("A")
                .signUpdate(signUpDate)
                .build();
        sut.save(member);

        Boolean result = sut.existsServiceId("ZZ");

        assert !result;
    }

    @Test
    @DisplayName("사용자의 서비스 아이디가 있다면 true를 반환한다.")
    public void Return1_If_ExistsNickname() {
        LocalDateTime signUpDate = LocalDateTime.of(2022, 10, 1, 1, 1, 1);
        Member member = Member.builder()
                .serviceId("A")
                .nickname("A")
                .password("A")
                .signUpdate(signUpDate)
                .build();
        sut.save(member);

        Boolean result = sut.existsNickname(member.getNickname());

        assert result;
    }

    @Test
    @DisplayName("사용자의 서비스 아이디가 없다면 false를 반환한다.")
    public void Return0_If_NotExistsNickname() {
        LocalDateTime signUpDate = LocalDateTime.of(2022, 10, 1, 1, 1, 1);
        Member member = Member.builder()
                .serviceId("A")
                .nickname("A")
                .password("A")
                .signUpdate(signUpDate)
                .build();
        sut.save(member);

        Boolean result = sut.existsNickname("BZ");

        assert !result;
    }

    @Test
    @DisplayName("서비스 아이디는 일치하나 사용자의 다른 패스워드로 조회할 때 false를 반환한다.")
    public void return_false_if_notExistsMember() {
        LocalDateTime signUpDate = LocalDateTime.of(2022, 10, 1, 1, 1, 1);
        Member member = Member.builder()
                .serviceId("A")
                .nickname("A")
                .password("B")
                .signUpdate(signUpDate)
                .build();
        sut.save(member);

        Boolean result = sut.existsMemberWithServiceIdAndPassword("A", "A");

        assert !result;
    }

    @Test
    @DisplayName("서비스 아이디와 패스워드로 검색한 사용자가 존재한다면 true를 반환한다.")
    public void return_true_if_existsMember() {
        LocalDateTime signUpDate = LocalDateTime.of(2022, 10, 1, 1, 1, 1);
        Member member = Member.builder()
                .serviceId("A")
                .nickname("A")
                .password("A")
                .signUpdate(signUpDate)
                .build();
        sut.save(member);

        Boolean result = sut.existsMemberWithServiceIdAndPassword("A", "A");

        assert result;
    }

    @Test
    @DisplayName("존재하지 않는 서비스 아이디와 패스워드로 검색한다면 false를 반환한다.")
    public void return_false_if_not_exists_id_and_password() {
        LocalDateTime signUpDate = LocalDateTime.of(2022, 10, 1, 1, 1, 1);
        Member member = Member.builder()
                .serviceId("A")
                .nickname("A")
                .password("A")
                .signUpdate(signUpDate)
                .build();
        sut.save(member);

        Boolean result = sut.existsMemberWithServiceIdAndPassword("FWf135", "vWE3");

        assert !result;
    }
}