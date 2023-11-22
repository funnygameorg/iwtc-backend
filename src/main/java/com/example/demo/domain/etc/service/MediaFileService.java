package com.example.demo.domain.etc.service;

import com.example.demo.domain.etc.component.MediaFileComponent;
import com.example.demo.domain.etc.component.MediaFileComponent.AllFieldMediaFileDto;
import com.example.demo.domain.etc.controller.response.MediaFileResponse;
import com.example.demo.domain.etc.exception.NotFoundMediaFIleException;
import com.example.demo.domain.etc.model.MediaFile;
import com.example.demo.domain.etc.repository.MediaFileRepository;
import com.example.demo.infra.s3.S3Component;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MediaFileService {

    private final MediaFileRepository mediaFileRepository;
    private final S3Component s3Component;
    private final MediaFileComponent mediaFileComponent;



    public MediaFileResponse getMediaFile(long mediaFileId) throws IOException {

        MediaFile mediaFile = mediaFileRepository
                .findById(mediaFileId)
                .orElseThrow(() -> new NotFoundMediaFIleException(mediaFileId));

        String mediaFileBody = s3Component.getObject(mediaFile.getFilePath());

        AllFieldMediaFileDto allFieldMediaFileDto = mediaFileComponent.convertToTotalDataMediaFile(mediaFile);

        return MediaFileResponse.fromEntity(allFieldMediaFileDto, mediaFileBody);
    }



}
