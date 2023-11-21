package com.example.demo.infra;

import org.springframework.stereotype.Component;

@Component
public class S3Service {

    public String uploadFile(String file) {
        return "resourcePath : " + file.substring(0, 20);
    }
}
