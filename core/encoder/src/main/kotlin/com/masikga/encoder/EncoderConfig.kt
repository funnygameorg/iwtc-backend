package com.masikga.encoder

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
@EnableEncryptableProperties
class EncoderConfig {
    // 설정 파일 암호화
    @Bean("jasyptEncryptorAES")
    fun stringEncryptor(): org.jasypt.encryption.StringEncryptor {
        val encryptor = PooledPBEStringEncryptor()
        val config = SimpleStringPBEConfig()
        config.setPassword("gdh-password")
        config.setAlgorithm("PBEWITHHMACSHA512ANDAES_256")
        config.setKeyObtentionIterations("1000")
        config.setPoolSize("1")
        config.setProviderName("SunJCE")
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator")
        config.setIvGeneratorClassName("org.jasypt.iv.RandomIvGenerator")
        config.setStringOutputType("base64")
        encryptor.setConfig(config)
        return encryptor
    }

    // 사용자 패스워드 암호화
    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder()
    }
}