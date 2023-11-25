package com.example.demo.domain.etc.service;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.example.demo.common.error.CustomErrorCode;
import com.example.demo.domain.etc.component.MediaFileComponent;
import com.example.demo.domain.etc.component.MediaFileComponent.AllFieldMediaFileDto;
import com.example.demo.domain.etc.controller.response.MediaFileResponse;
import com.example.demo.domain.etc.exception.FailedGetS3MediaDataException;
import com.example.demo.domain.etc.exception.NotFoundMediaFIleException;
import com.example.demo.domain.etc.model.MediaFile;
import com.example.demo.domain.etc.repository.MediaFileRepository;
import com.example.demo.infra.s3.S3Component;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.print.attribute.standard.Media;
import java.io.IOException;
import java.util.stream.LongStream;

import static com.example.demo.common.error.CustomErrorCode.NOT_EXISTS_S3_MEDIA_FILE;
import static com.example.demo.common.error.CustomErrorCode.SERVER_INTERNAL_ERROR;

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

        AllFieldMediaFileDto allFieldMediaFileDto = mediaFileComponent.convertToTotalDataMediaFile(mediaFile);

        return MediaFileResponse.fromEntity(allFieldMediaFileDto, mediaFileBody);
    }





    private String getMediaFileBody(MediaFile mediaFile) {

        String mediaFileBody = null;

        try {

            mediaFileBody = s3Component.getObject(mediaFile.getObjectKey());

        } catch (AmazonS3Exception ex) {

            throw new FailedGetS3MediaDataException(mediaFile.getObjectKey(), NOT_EXISTS_S3_MEDIA_FILE);

        } catch (IOException ex) {

            throw new FailedGetS3MediaDataException(mediaFile.getObjectKey(), SERVER_INTERNAL_ERROR);
        }
        return mediaFileBody;
    }




}
