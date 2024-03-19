package com.masikga.feign

import feign.codec.ErrorDecoder
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.cloud.openfeign.FeignAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableFeignClients
@ImportAutoConfiguration(FeignAutoConfiguration::class)
class FeignClientConfig {

    @Bean
    fun errorDecoder(): ErrorDecoder = CustomErrorDecoder()

}