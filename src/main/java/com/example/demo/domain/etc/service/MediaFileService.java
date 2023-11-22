package com.example.demo.domain.etc.service;

import com.example.demo.domain.etc.exception.NotFoundMediaFIleException;
import com.example.demo.domain.etc.model.MediaFile;
import com.example.demo.domain.etc.repository.MediaFileRepository;
import com.example.demo.infra.s3.S3Component;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MediaFileService {

    private final MediaFileRepository mediaFileRepository;
    private final S3Component s3Component;


    public String getMediaFile(long mediaFileId) {
        MediaFile mediaFile = mediaFileRepository
                .findById(mediaFileId)
                .orElseThrow(() -> new NotFoundMediaFIleException(mediaFileId));

        return s3Component.getBase64Object(mediaFile.getFilePath());

    }

}
