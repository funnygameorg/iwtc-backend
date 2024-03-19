package com.masikga.feign

class FeignException(
    val code: Int,
    val customMessage: String
) : RuntimeException()