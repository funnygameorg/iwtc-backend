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
	 *
	 * @param mediaFile 데이터를 얻고싶은 미디어 파일
	 * @return 미디어파일 상속구조에 포함된 모든 컬럼을 반환 (없는 컬럼은 null 반환)
	 */
	public AllFieldMediaFileDto convertToTotalDataMediaFile(MediaFile mediaFile) {

		return new AllFieldMediaFileDto(
			mediaFile.getId(),
			mediaFile.getFileType(),
			mediaFile.getObjectKey(),
			mediaFile instanceof StaticMediaFile ? ((StaticMediaFile)mediaFile).getOriginalName() : null,
			mediaFile instanceof InternetVideoUrl ? ((InternetVideoUrl)mediaFile).getVideoStartTime() : null,
			mediaFile instanceof InternetVideoUrl ? ((InternetVideoUrl)mediaFile).getVideoPlayDuration() : null,
			mediaFile.getCreatedAt(),
			mediaFile.getUpdatedAt()
		);

	}

	public record AllFieldMediaFileDto(
		long id,
		FileType fileType,

		String mediaPath,
		String originalName,
		String videoStartTime,
		Integer videoPlayDuration,
		LocalDateTime createdAt,
		LocalDateTime updatedAt
	) {
	}
}
