package com.masikga.model.common

data class RestApiResponse<T>(
    val code: Int,
    val message: String,
    val data: T
)