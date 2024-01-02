package com.example.demo.domain.member.repository;

public interface MemberQueryRepository {

	Boolean existsNickname(String nickname);

	Boolean existsServiceId(String serviceId);
}
