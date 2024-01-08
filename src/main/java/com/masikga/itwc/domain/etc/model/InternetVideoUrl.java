package com.masikga.itwc.domain.etc.model;

import static jakarta.persistence.EnumType.*;
import static lombok.AccessLevel.*;

import java.util.Objects;

import org.hibernate.annotations.Comment;

import com.masikga.itwc.common.error.exception.NotNullArgumentException;
import com.masikga.itwc.domain.etc.exception.NotSupportedFileExtensionException;
import com.masikga.itwc.domain.etc.model.vo.FileType;
import com.masikga.itwc.domain.etc.model.vo.MediaFileExtension;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * startTime example
 * 1. 00001 : 000분 01초
 * 2. 00101 : 001분 01초
 * 3. 02000 : 020분 00초
 * 4. 00113 : 001분 13초
 */
@Getter
@Entity
@Table(name = "internet_video_url")
@NoArgsConstructor(access = PROTECTED)
public class InternetVideoUrl extends MediaFile {

	@Comment("현재 실행가능한 영상인가?") // 스케쥴러로 비정상 컨텐츠 표시읽고 사용자 알림으로 알려주기
	private boolean isPlayableVideo;

	@NotNull
	@Column(length = 5)
	@Comment("영상 시작 시간, 0부터 9까지 이루어진 5글자의 숫자만 허용, 앞 3자리 숫자는 `분`을 표현하며 뒤 2자리는 `초`를 표현")
	@Pattern(regexp = "\\d{5}")
	private String videoStartTime;

	@NotNull
	@Comment("영상 반복 구간, 2부터 5까지 이루어진 1글자의 숫자만 허용하며 단위는 초")
	@Min(value = 2, message = "영상 재생 시간은 2 ~ 5초 입니다.")
	@Max(value = 5, message = "영상 재생 시간은 2 ~ 5초 입니다.")
	private Integer videoPlayDuration;

	// TODO : MediaFile(부모 클래스)로 이동하기
	@Comment("동영상의 형태 (ex : 유튜브 링크, 네이버 비디오 링크...)")
	@Enumerated(STRING)
	private MediaFileExtension videoDetailType;

	public void update(String objectKey, String videoStartTime, Integer videoPlayDuration, String detailFileType) {

		if (Objects.isNull(objectKey) || Objects.isNull(videoStartTime) || Objects.isNull(videoPlayDuration)) {
			throw new NotNullArgumentException(objectKey, videoStartTime, videoPlayDuration);
		}

		if (!MediaFileExtension.isSupportedType(detailFileType)) {
			throw new NotSupportedFileExtensionException(detailFileType);
		}

		super.objectKey = objectKey;
		this.videoStartTime = videoStartTime;
		this.videoPlayDuration = videoPlayDuration;
		this.videoDetailType = MediaFileExtension.valueOf(detailFileType);

	}

	@Builder
	private InternetVideoUrl(Long id, String objectKey, boolean isPlayableVideo, String videoStartTime,
		Integer videoPlayDuration, String bucketName, String videoDetailType) {

		super(id, objectKey, FileType.INTERNET_VIDEO_URL, bucketName);

		if (Objects.isNull(objectKey) || Objects.isNull(videoStartTime) || Objects.isNull(videoPlayDuration)) {
			throw new NotNullArgumentException(objectKey, videoStartTime, videoPlayDuration);
		}

		if (!MediaFileExtension.isSupportedType(videoDetailType)) {
			throw new NotSupportedFileExtensionException(videoDetailType);
		}

		this.isPlayableVideo = isPlayableVideo;
		this.videoStartTime = videoStartTime;
		this.videoPlayDuration = videoPlayDuration;
		this.videoDetailType = MediaFileExtension.valueOf(videoDetailType);

	}
}
