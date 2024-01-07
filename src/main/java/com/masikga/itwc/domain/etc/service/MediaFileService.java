package com.masikga.itwc.domain.etc.service;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.masikga.itwc.domain.etc.component.MediaFileComponent;
import com.masikga.itwc.domain.etc.controller.response.MediaFileResponse;
import com.masikga.itwc.domain.etc.exception.FailedGetS3MediaDataException;
import com.masikga.itwc.domain.etc.exception.NotFoundMediaFIleException;
import com.masikga.itwc.domain.etc.model.MediaFile;
import com.masikga.itwc.domain.etc.repository.MediaFileRepository;
import com.masikga.itwc.infra.filestorage.S3Component;
import com.masikga.itwc.common.error.CustomErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MediaFileService {

	private final MediaFileRepository mediaFileRepository;
	private final S3Component s3Component;
	private final MediaFileComponent mediaFileComponent;

	public MediaFileResponse getMediaFile(long mediaFileId) {

		MediaFile mediaFile = mediaFileRepository
			.findById(mediaFileId)
			.orElseThrow(() -> new NotFoundMediaFIleException(mediaFileId));

		String mediaFileBody = getMediaFileBody(mediaFile);

		MediaFileComponent.AllFieldMediaFileDto allFieldMediaFileDto = mediaFileComponent.convertToTotalDataMediaFile(mediaFile);

		return MediaFileResponse.fromEntity(allFieldMediaFileDto, mediaFileBody);
	}

	public List<MediaFileResponse> getMediaFile(List<Long> mediaFileIds) {

		List<MediaFile> mediaFiles = mediaFileRepository
			.findAllById(mediaFileIds);

		return mediaFiles.stream().map(mediaFile -> {

			var mediaFileBody = getMediaFileBody(mediaFile);

			var allFieldMediaFileDto = mediaFileComponent.convertToTotalDataMediaFile(mediaFile);

			return MediaFileResponse.fromEntity(allFieldMediaFileDto, mediaFileBody);

		}).toList();

	}

	private String getMediaFileBody(MediaFile mediaFile) {

		String mediaFileBody = null;

		try {

			mediaFileBody = s3Component.getObject(mediaFile.getObjectKey());

		} catch (AmazonS3Exception ex) {

			throw new FailedGetS3MediaDataException(mediaFile.getObjectKey(), CustomErrorCode.NOT_EXISTS_S3_MEDIA_FILE);

		} catch (IOException ex) {

			throw new FailedGetS3MediaDataException(mediaFile.getObjectKey(), CustomErrorCode.SERVER_INTERNAL_ERROR);
		}
		return mediaFileBody;
	}

}
