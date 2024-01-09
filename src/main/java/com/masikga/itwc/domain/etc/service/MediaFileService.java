package com.masikga.itwc.domain.etc.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.masikga.itwc.domain.etc.component.MediaFileComponent;
import com.masikga.itwc.domain.etc.controller.response.MediaFileResponse;
import com.masikga.itwc.domain.etc.exception.NotFoundMediaFIleException;
import com.masikga.itwc.domain.etc.model.MediaFile;
import com.masikga.itwc.domain.etc.repository.MediaFileRepository;
import com.masikga.itwc.infra.filestorage.S3Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MediaFileService {

	private final MediaFileRepository mediaFileRepository;
	private final MediaFileComponent mediaFileComponent;
	private final S3Component s3Component;

	public MediaFileResponse getMediaFile(long mediaFileId) {

		MediaFile mediaFile = mediaFileRepository
			.findById(mediaFileId)
			.orElseThrow(() -> new NotFoundMediaFIleException(mediaFileId));

		var mediaFileBody = s3Component.getObject(mediaFile.getObjectKey());

		MediaFileComponent.AllFieldMediaFileDto allFieldMediaFileDto = mediaFileComponent.convertToTotalDataMediaFile(
			mediaFile);

		return MediaFileResponse.fromEntity(allFieldMediaFileDto, mediaFileBody);
	}

}
