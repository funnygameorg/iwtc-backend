package com.example.demo.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.example.demo.common.config.condition.NotProdConfigCondition;
import com.example.demo.common.config.condition.ProdConfigCondition;

@Configuration
public class S3Config {

	@Value("${cloud.aws.credentials.access-key}")
	private String accessKey;
	@Value("${cloud.aws.credentials.secret-key}")
	private String secretKey;
	@Value("${cloud.aws.region.static}")
	private String region;
	@Value("${cloud.aws.endpoint}")
	private String endpoint;

	// LocalStack S3 사용
	@Bean
	@Conditional(NotProdConfigCondition.class)
	public AmazonS3Client localAndTestAmazonS3Client() {

		AWSStaticCredentialsProvider provider =
			new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey));

		AwsClientBuilder.EndpointConfiguration endpointConfiguration =
			new AwsClientBuilder.EndpointConfiguration(endpoint, region);

		return (AmazonS3Client)AmazonS3ClientBuilder.standard()
			.withEndpointConfiguration(endpointConfiguration)
			.withCredentials(provider)
			.withPathStyleAccessEnabled(true)
			.build();
	}

	@Bean
	@Conditional(ProdConfigCondition.class)
	public AmazonS3Client prodAmazonS3Client() {

		AWSStaticCredentialsProvider provider =
			new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey));

		return (AmazonS3Client)AmazonS3ClientBuilder.standard()
			.withCredentials(provider)
			.withRegion(region)
			.build();
	}
}
