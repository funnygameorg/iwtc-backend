package com.masikga.model.member

import kotlin.annotation.AnnotationRetention.*
import kotlin.annotation.AnnotationTarget.*

@Retention(RUNTIME)
@Target(FUNCTION, VALUE_PARAMETER)
annotation class CustomAuthentication(val required: Boolean = true)
