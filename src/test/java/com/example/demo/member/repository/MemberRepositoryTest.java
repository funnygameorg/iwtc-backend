package com.example.demo.member.repository;

import com.example.demo.member.model.Member;
import com.example.demo.member.model.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

@ActiveProfiles("local")
@SpringBootTest
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository sut;

    @Test
    @DisplayName("사용자의 서비스 아이디가 있다면 1을 반환한다.")
    public void Return1_If_ExistsServiceId() {
        LocalDateTime signUpDate = LocalDateTime.of(2022, 10, 1, 1, 1, 1);
        Member member = Member.builder()
                .serviceId("A")
                .nickname("A")
                .password("A")
                .signUpdate(signUpDate)
                .build();
        sut.save(member);

        int result = sut.existsServiceId(member.getServiceId());

        assert result == 1;
    }

    @Test
    @DisplayName("사용자의 서비스 아이디가 없다면 0을 반환한다.")
    public void Return0_If_NotExistsServiceId() {
        LocalDateTime signUpDate = LocalDateTime.of(2022, 10, 1, 1, 1, 1);
        Member member = Member.builder()
                .serviceId("A")
                .nickname("A")
                .password("A")
                .signUpdate(signUpDate)
                .build();
        sut.save(member);

        int result = sut.existsServiceId("ZZ");

        assert result == 0;
    }

    @Test
    @DisplayName("사용자의 서비스 아이디가 있다면 1을 반환한다.")
    public void Return1_If_ExistsNickname() {
        LocalDateTime signUpDate = LocalDateTime.of(2022, 10, 1, 1, 1, 1);
        Member member = Member.builder()
                .serviceId("A")
                .nickname("A")
                .password("A")
                .signUpdate(signUpDate)
                .build();
        sut.save(member);

        int result = sut.existsNickname(member.getNickname());

        assert result == 1;
    }

    @Test
    @DisplayName("사용자의 서비스 아이디가 없다면 0을 반환한다.")
    public void Return0_If_NotExistsNickname() {
        LocalDateTime signUpDate = LocalDateTime.of(2022, 10, 1, 1, 1, 1);
        Member member = Member.builder()
                .serviceId("A")
                .nickname("A")
                .password("A")
                .signUpdate(signUpDate)
                .build();
        sut.save(member);

        int result = sut.existsNickname("BZ");

        assert result == 0;
    }
}