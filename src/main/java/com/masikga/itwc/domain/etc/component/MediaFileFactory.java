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
	 * @return `MediaFile` 구현체
	 */
	public MediaFile createMediaFile(
		FileType fileType,
		String objectKey,
		String originalName,
		Integer videoPlayDuration,
		String videoStartTime
	) {

		return switch (fileType) {
			case STATIC_MEDIA_FILE -> createStaticMediaFile(objectKey, originalName);
			case INTERNET_VIDEO_URL -> createInternetVideoUrl(objectKey, videoPlayDuration, videoStartTime);
		};

	}

	private StaticMediaFile createStaticMediaFile(String objectKey, String originalName) {
		return StaticMediaFile.builder()
			.objectKey(objectKey)
			.extension("tempExtension")
			.originalName(originalName)
			.build();
	}

	private InternetVideoUrl createInternetVideoUrl(String objectKey, int videoPlayDuration, String videoStartTime) {
		return InternetVideoUrl.builder()
			.objectKey(objectKey)
			.isPlayableVideo(true)
			.videoPlayDuration(videoPlayDuration)
			.videoStartTime(videoStartTime)
			.build();
	}

}
