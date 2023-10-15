package com.example.demo.member.model;

import org.springframework.stereotype.Repository;

@Repository
public interface MemberQueryRepository {

    int existsNickname(String nickname);
    int existsServiceId(String serviceId);
}
