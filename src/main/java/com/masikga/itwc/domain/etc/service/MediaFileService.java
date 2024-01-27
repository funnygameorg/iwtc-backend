package com.masikga.itwc.domain.etc.service;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.masikga.itwc.domain.etc.component.MediaFileComponent;
import com.masikga.itwc.domain.etc.controller.response.MediaFileResponse;
import com.masikga.itwc.domain.etc.exception.NotFoundMediaFIleException;
import com.masikga.itwc.domain.etc.model.MediaFile;
import com.masikga.itwc.domain.etc.repository.MediaFileRepository;
import com.masikga.itwc.infra.filestorage.FileStorage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MediaFileService {

	private final MediaFileRepository mediaFileRepository;
	private final MediaFileComponent mediaFileComponent;
	private final FileStorage s3Component;

	private static final String THUMBNAIL_DIVIDE_2_BUCKET_NAME = "-divide2";

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	/**
	 * 미디어 파일을 조회합니다.
	 *
	 * @param mediaFileId 조회하기 원하는 미디어 파일의 식별자
	 * @param size        이미지 크기
	 * @return 미디어 파일
	 */
	public MediaFileResponse getMediaFile(long mediaFileId, String size) {

		MediaFile mediaFile = mediaFileRepository
			.findById(mediaFileId)
			.orElseThrow(() -> new NotFoundMediaFIleException(mediaFileId));

		String mediaFileBody = getMediaFileBody(mediaFile.getObjectKey(), size);

		MediaFileComponent.AllFieldMediaFileDto allFieldMediaFileDto = mediaFileComponent.convertToTotalDataMediaFile(
			mediaFile);

		return MediaFileResponse.fromEntity(allFieldMediaFileDto, mediaFileBody);
	}

	/**
	 * 요청한 사이즈에 맞는 이미지를 반환합니다.
	 * 원하는 사이즈의 썸네일이 없는 경우 그나마 큰 것(3 -> 2 -> 원본)이라도 반환합니다.
	 *
	 * @param objectKey S3 오브젝트 키
	 * @return 미디어 파일 바디
	 */
	private String getMediaFileBody(String objectKey, String size) {

		String mediaFileBody = null;

		/**
		 * 절반 사이즈 썸네일
		 * 1/2사이즈를 얻을 수 없다면 원본 사이즈라도 반환한다.
		 **/
		if (Objects.equals(size, "divide2")) {

			try {
				mediaFileBody = s3Component.getObject(objectKey, bucket + THUMBNAIL_DIVIDE_2_BUCKET_NAME);
			} catch (RuntimeException ex) {
				mediaFileBody = s3Component.getObject(objectKey, bucket);
			}

			// 원본 사이즈 이미지
		} else {

			mediaFileBody = s3Component.getObject(objectKey, bucket);

		}

		return mediaFileBody;
	}

}
