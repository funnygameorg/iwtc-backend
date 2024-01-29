package com.masikga.itwc.domain.etc.component;

import org.springframework.stereotype.Component;

import com.masikga.itwc.domain.etc.model.InternetVideoUrl;
import com.masikga.itwc.domain.etc.model.MediaFile;
import com.masikga.itwc.domain.etc.model.StaticMediaFile;
import com.masikga.itwc.domain.etc.model.vo.FileType;

@Component
public class MediaFileFactory {

	/***
	 * `mediaFile`을 생성합니다.
	 * `mediaFile`의 모든 구현체에 생성에 필요한 모든 정보를 받습니다.
	 *
	 * @param fileType 생성되는 미디어 파일의 타입
	 * @param objectKey 오브젝트 키
	 * @param originalName 사용자가 제공한 원래 파일 이름 (`staticMediaFile`에서 사용)
	 * @param videoPlayDuration 영상 반복 구간 (`InternetVideoUrl`에서 사용)
	 * @param videoStartTime 영상 시작 시간 (`InternetVideoUrl`에서 사용)
	 * @param detailFileType 미디어 파일의 자세한 형태 (PNG, GIF, YOU_TUBE...)
	 * @return `MediaFile` 구현체
	 */
	public MediaFile createMediaFile(
		String objectKey,
		String originalName,
		Integer videoPlayDuration,
		String videoStartTime,
		FileType fileType,
		String detailFileType,
		String originalFileSize) {

		return switch (fileType) {
			case STATIC_MEDIA_FILE -> createStaticMediaFile(objectKey, originalName, detailFileType, originalFileSize);
			case INTERNET_VIDEO_URL ->
				createInternetVideoUrl(objectKey, videoPlayDuration, videoStartTime, detailFileType, originalFileSize);
		};

	}

	private StaticMediaFile createStaticMediaFile(String objectKey, String originalName, String detailFileType,
		String originalFileSize) {

		return StaticMediaFile.builder()
			.objectKey(objectKey)
			.originalName(originalName)
			.extension(detailFileType)
			.originalFileSize(originalFileSize)
			.build();
	}

	private InternetVideoUrl createInternetVideoUrl(String objectKey, int videoPlayDuration, String videoStartTime,
		String detailFileType, String originalFileSize) {

		return InternetVideoUrl.builder()
			.objectKey(objectKey)
			.isPlayableVideo(true)
			.videoPlayDuration(videoPlayDuration)
			.videoStartTime(videoStartTime)
			.videoDetailType(detailFileType)
			.originalFileSize(originalFileSize)
			.build();
	}

}
