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
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3Component {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3Client s3Client;




    /**
     * S3에 문자열을 저장한다.
     *
     * @param stringFormatFile S3에 저장하는 파일
     * @param key 파일 식별자
     * @return
     */
    public PutObjectResult putObject(String stringFormatFile, String key) {

        InputStream inputStream = new ByteArrayInputStream(stringFormatFile.getBytes(UTF_8));

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(stringFormatFile.length());

        PutObjectRequest request = new PutObjectRequest(bucket, key, inputStream, metadata);

        return s3Client.putObject(request);

    }





    /**
     * `Key`값을 사용하여 저장한 파일을 조회한다.
     *
     * @param objectKey 찾기위한 파일의 식별자
     * @return 파일 내용
     * @throws IOException byte[] 형식의 조회한 파일을 문자열로 컨버트할 때 발생할 수 있음
     * @throws AmazonS3Exception S3 라이브러리를 사용할 때 발생할 수 있음
     */
    public String getObject(String objectKey) throws IOException, AmazonS3Exception {

        S3Object object = s3Client.getObject(bucket, objectKey);


        try (S3ObjectInputStream ois = object.getObjectContent()) {

            byte[] readBytes = ois.readAllBytes();

            return new String(readBytes, UTF_8);
        }

    }





}
