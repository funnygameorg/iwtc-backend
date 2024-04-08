package com.masikga.model.member

data class GetMemberResponse(
    val memberId: Long,
    val serviceId: String,
    val nickname: String,
    val password: String
)