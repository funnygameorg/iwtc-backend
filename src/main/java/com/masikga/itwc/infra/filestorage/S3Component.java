package com.masikga.itwc.infra.filestorage;

import static com.masikga.itwc.common.error.CustomErrorCode.*;
import static java.nio.charset.StandardCharsets.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.masikga.itwc.domain.etc.exception.FailedGetS3MediaDataException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
	 * @throws IOException       byte[] 형식의 조회한 파일을 문자열로 컨버트할 때 발생할 수 있음
	 * @throws AmazonS3Exception S3 라이브러리를 사용할 때 발생할 수 있음
	 */
	public String getObject(String objectKey) {

		S3Object object = s3Client.getObject(bucket, objectKey);

		try (S3ObjectInputStream ois = object.getObjectContent()) {

			byte[] readBytes = ois.readAllBytes();

			return new String(readBytes, UTF_8);

		} catch (AmazonS3Exception ex) {

			throw new FailedGetS3MediaDataException(objectKey, NOT_EXISTS_S3_MEDIA_FILE);

		} catch (IOException ex) {

			throw new FailedGetS3MediaDataException(objectKey, SERVER_INTERNAL_ERROR);

		}

	}

}
