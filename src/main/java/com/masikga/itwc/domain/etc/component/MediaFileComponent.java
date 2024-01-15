package com.masikga.itwc.domain.etc.component;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.masikga.itwc.domain.etc.model.InternetVideoUrl;
import com.masikga.itwc.domain.etc.model.MediaFile;
import com.masikga.itwc.domain.etc.model.StaticMediaFile;
import com.masikga.itwc.domain.etc.model.vo.FileType;

@Component
public class MediaFileComponent {

	/**
	 * MediaFile 설계를 잘못해서 임시로 사용합니다.
	 * 미디어 파일의 상속에 포함된 미디어 파일 클래스의 모든 정보를 얻습니다.
	 *
	 * @param mediaFile 데이터를 얻고싶은 미디어 파일
	 * @return 미디어파일 상속구조에 포함된 모든 컬럼을 반환 (없는 컬럼은 null 반환)
	 */
	public AllFieldMediaFileDto convertToTotalDataMediaFile(MediaFile mediaFile) {

		var imageOriginalName = getOriginalName(mediaFile);
		var videoStartTime = getVideoStartTime(mediaFile);
		var videoPlayDuration = getVideoPlayDuration(mediaFile);

		return new AllFieldMediaFileDto(
			mediaFile.getId(),
			mediaFile.getFileType(),
			mediaFile.getObjectKey(),
			imageOriginalName,
			videoStartTime,
			videoPlayDuration,
			mediaFile.getDetailType().name(),
			mediaFile.getCreatedAt(),
			mediaFile.getUpdatedAt()
		);

	}

	// 파일이 비디오 파일인 경우 실제 값을 반환, 이미지 형식인 경우 null을 반환
	private Integer getVideoPlayDuration(MediaFile mediaFile) {
		return mediaFile instanceof InternetVideoUrl ? ((InternetVideoUrl)mediaFile).getVideoPlayDuration() : null;
	}

	// 파일이 비디오 파일인 경우 실제 값을 반환, 이미지 형식인 경우 null을 반환
	private String getVideoStartTime(MediaFile mediaFile) {
		return mediaFile instanceof InternetVideoUrl ? ((InternetVideoUrl)mediaFile).getVideoStartTime() : null;
	}

	// 파일이 이미지 파일인 경우 실제 값을 반환, 비디오 형식인 경우 null을 반환
	private String getOriginalName(MediaFile mediaFile) {
		return mediaFile instanceof StaticMediaFile ? ((StaticMediaFile)mediaFile).getOriginalName() : null;
	}

	public record AllFieldMediaFileDto(
		long id,
		FileType fileType,

		String mediaPath,
		String originalName,
		String videoStartTime,
		Integer videoPlayDuration,
		String detailType,
		LocalDateTime createdAt,
		LocalDateTime updatedAt
	) {
	}
}
