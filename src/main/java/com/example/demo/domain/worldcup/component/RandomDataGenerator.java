package com.example.demo.domain.worldcup.component;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RandomDataGenerator implements RandomDataGeneratorInterface{

    @Override
    public String generate() {
        return UUID.randomUUID().toString();
    }
}
