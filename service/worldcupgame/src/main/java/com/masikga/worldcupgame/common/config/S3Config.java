package com.masikga.worldcupgame.common.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.masikga.worldcupgame.common.config.condition.NotProdConfigCondition;
import com.masikga.worldcupgame.common.config.condition.ProdConfigCondition;
import com.masikga.worldcupgame.common.config.property.S3Property;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class S3Config {

	private final S3Property s3Property;

	// LocalStack S3 사용
	@Bean
	@Conditional(NotProdConfigCondition.class)
	public AmazonS3Client localAndTestAmazonS3Client() {

		AWSStaticCredentialsProvider provider =
			new AWSStaticCredentialsProvider(new BasicAWSCredentials(s3Property.credentials().accessKey(),
				s3Property.credentials().secretKey()));

		AwsClientBuilder.EndpointConfiguration endpointConfiguration =
			new AwsClientBuilder.EndpointConfiguration(s3Property.endpoint(), s3Property.region());

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
			new AWSStaticCredentialsProvider(new BasicAWSCredentials(s3Property.credentials().accessKey(),
				s3Property.credentials().secretKey()));

		return (AmazonS3Client)AmazonS3ClientBuilder.standard()
			.withCredentials(provider)
			.withRegion(s3Property.region())
			.build();
	}
}
