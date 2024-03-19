package com.masikga.worldcupgame.domain.etc.service;

import com.masikga.worldcupgame.domain.etc.component.MediaFileComponent;
import com.masikga.worldcupgame.domain.etc.controller.response.MediaFileResponse;
import com.masikga.worldcupgame.domain.etc.exception.NotFoundMediaFIleExceptionMember;
import com.masikga.worldcupgame.domain.etc.model.MediaFile;
import com.masikga.worldcupgame.domain.etc.model.vo.FileType;
import com.masikga.worldcupgame.domain.etc.repository.MediaFileRepository;
import com.masikga.worldcupgame.infra.filestorage.FileStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static com.masikga.worldcupgame.domain.etc.model.vo.FileType.STATIC_MEDIA_FILE;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MediaFileService {

    private final MediaFileRepository mediaFileRepository;
    private final MediaFileComponent mediaFileComponent;
    private final FileStorage s3Component;

    private static final String THUMBNAIL_DIVIDE_2_BUCKET_NAME = "-divide2";

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    /**
     * 미디어 파일을 조회합니다.
     *
     * @param mediaFileId 조회하기 원하는 미디어 파일의 식별자
     * @param size        이미지 크기
     * @return 미디어 파일
     */
    public MediaFileResponse getMediaFile(long mediaFileId, String size) {

        MediaFile mediaFile = mediaFileRepository
                .findById(mediaFileId)
                .orElseThrow(() -> new NotFoundMediaFIleExceptionMember(mediaFileId));

        String mediaFileBody = getMediaFileBody(mediaFile.getObjectKey(), mediaFile.getFileType(), size);

        MediaFileComponent.AllFieldMediaFileDto allFieldMediaFileDto = mediaFileComponent.convertToTotalDataMediaFile(
                mediaFile);

        return MediaFileResponse.fromEntity(allFieldMediaFileDto, mediaFileBody);
    }

    /**
     * 요청한 사이즈에 맞는 이미지를 반환합니다.
     * 1. 원하는 사이즈의 썸네일이 없는 경우 그나마 큰 것(3 -> 2 -> 원본)이라도 반환합니다.
     * 2. 영상 링크의 경우 원본 파일 버킷에서 조회합니다.
     *
     * @param objectKey S3 오브젝트 키
     * @return 미디어 파일 바디
     */
    private String getMediaFileBody(String objectKey, FileType fileType, String size) {

        String mediaFileBody = null;

        /**
         * 반환 값 : 절반 사이즈 썸네일
         * 1/2사이즈를 얻을 수 없다면 원본 사이즈라도 반환한다. 원본 사이즈도 없다면 null을 반환한다.
         **/
        if (Objects.equals(size, "divide2") && fileType == STATIC_MEDIA_FILE) {

            try {
                mediaFileBody = s3Component.getObject(objectKey, bucket + THUMBNAIL_DIVIDE_2_BUCKET_NAME);
            } catch (RuntimeException ex) {

                try {
                    mediaFileBody = s3Component.getObject(objectKey, bucket);
                } catch (RuntimeException ex2) {
                    // TODO : 로그 추가하기
                }
            }

            /**
             * 반환 값 : 원본 사이즈 이미지
             */
        } else {

            mediaFileBody = s3Component.getObject(objectKey);

        }

        return mediaFileBody;
    }

}
