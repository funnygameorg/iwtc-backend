package com.masikga.itwc.infra.filestorage;

import static java.nio.charset.StandardCharsets.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileStorage {

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	private final AmazonS3Client s3Client;

	/**
	 * S3에 문자열을 저장한다.
	 *
	 * @param stringFormatFile S3에 저장하는 파일
	 * @param key              파일 식별자
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
	 */
	public String getObject(String objectKey) {

		try (S3Object object = s3Client.getObject(bucket, objectKey)) {

			try (S3ObjectInputStream ois = object.getObjectContent()) {

				byte[] readBytes = ois.readAllBytes();

				return new String(readBytes, UTF_8);

			}

		} catch (Exception ex) {

			throw new RuntimeException(ex);
		}

	}

	/**
	 * `Key`값과 `Bucket`값을 사용하여 저장한 파일을 조회한다.
	 *
	 * @param objectKey 찾기위한 파일의 식별자
	 * @return 파일 내용
	 */
	public String getObject(String objectKey, String targetBucket) {

		try (S3Object object = s3Client.getObject(targetBucket, objectKey)) {

			try (S3ObjectInputStream ois = object.getObjectContent()) {

				byte[] readBytes = ois.readAllBytes();

				return new String(readBytes, UTF_8);

			}

		} catch (Exception ex) {

			throw new RuntimeException(ex);
		}

	}

}
