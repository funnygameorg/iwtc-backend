package com.masikga.member.repository.impl.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {

    Boolean existsNickname(String nickname);

    Boolean existsServiceId(String serviceId);

}
