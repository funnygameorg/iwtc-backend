package com.example.demo.infra.s3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.Base64;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Base64Util;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.*;
import java.util.UUID;

import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3Component {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3Client s3Client;




    public PutObjectResult putObject(String stringFormatFile, String key) {

        InputStream inputStream = new ByteArrayInputStream(stringFormatFile.getBytes(UTF_8));

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(stringFormatFile.length());

        PutObjectRequest request = new PutObjectRequest(bucket, key, inputStream, metadata);

        return s3Client.putObject(request);

    }



    public String getBase64Object(String objectKey) {

        S3Object object = s3Client.getObject(bucket, objectKey);

        log.info(" 조회 오브젝트 {}", object);

        S3ObjectInputStream ois = object.getObjectContent();

        try {

            byte[] readBytes = ois.readAllBytes();
            log.info("바이트 결과 {}", readBytes);

            String result = Base64.encodeAsString(readBytes);
            log.info("이미지 결과 {}", result);
            return result;

        } catch (IOException e) {

            log.info("이미지 실패 {}", e.getMessage());
            throw new RuntimeException(e);

        }

    }



}
