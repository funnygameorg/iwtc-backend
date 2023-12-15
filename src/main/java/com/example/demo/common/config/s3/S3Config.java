package com.example.demo.common.config.s3;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
    실제 AWS S3 연결
 */
@Configuration
@ConditionalOnProperty(name = "spring.profiles.active", havingValue = "prod")
public class S3Config {

    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;
    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;
    @Value("${cloud.aws.region.static}")
    private String region;

    @Bean
    public AmazonS3Client amazonS3Client() {

        AWSStaticCredentialsProvider provider =
                new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey));

        return (AmazonS3Client) AmazonS3ClientBuilder.standard()
                .withCredentials(provider)
                .withRegion(region)
                .build();
    }

}
