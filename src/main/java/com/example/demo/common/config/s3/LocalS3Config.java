package com.example.demo.common.config.s3;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;


/*
    localstack S3 연결
 */
@Configuration
@Conditional(NotProdS3ConfigCondition.class)
public class LocalS3Config {

    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;
    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;
    @Value("${cloud.aws.region.static}")
    private String region;
    @Value("${cloud.aws.endpoint}")
    private String endpoint;




    @Bean
    public AmazonS3Client amazonS3Client() {

        AWSStaticCredentialsProvider provider =
                new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey));

        AwsClientBuilder.EndpointConfiguration endpointConfiguration =
                new AwsClientBuilder.EndpointConfiguration(endpoint, region);

        return (AmazonS3Client) AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(endpointConfiguration)
                .withCredentials(provider)
                .withPathStyleAccessEnabled(true)
                .build();
    }




}
