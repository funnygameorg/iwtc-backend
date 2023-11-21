package com.example.demo.domain.etc.component;

import com.example.demo.domain.etc.model.InternetVideoUrl;
import com.example.demo.domain.etc.model.MediaFile;
import com.example.demo.domain.etc.model.StaticMediaFile;
import com.example.demo.domain.etc.model.vo.FileType;
import org.springframework.stereotype.Component;

import static com.example.demo.domain.etc.model.vo.FileType.INTERNET_VIDEO_URL;
import static com.example.demo.domain.etc.model.vo.FileType.STATIC_MEDIA_FILE;

@Component
public class MediaFileFactory {


    /***
     * `mediaFile`을 생성합니다.
     * `mediaFile`의 모든 구현체에 생성에 필요한 모든 정보를 받습니다.
     *
     * @param fileType 생성되는 미디어 파일의 타입
     * @param mediaPath 미디어 파일 주소
     * @param originalName 사용자가 제공한 원래 파일 이름 (`staticMediaFile`에서 사용)
     * @param absoluteName S3에 저장되는 파일 이름 (`staticMediaFile`에서 사용)
     * @param videoPlayDuration 영상 반복 구간 (`InternetVideoUrl`에서 사용)
     * @param videoStartTime 영상 시작 시간 (`InternetVideoUrl`에서 사용)
     * @return `MediaFile` 구현체
     */
    public MediaFile createMediaFile(
            FileType fileType,
            String mediaPath,
            String originalName,
            String absoluteName,
            Integer videoPlayDuration,
            String videoStartTime
    ) {

        return switch (fileType) {
            case STATIC_MEDIA_FILE -> createStaticMediaFile(mediaPath, originalName, absoluteName);
            case INTERNET_VIDEO_URL -> createInternetVideoUrl(mediaPath, videoPlayDuration, videoStartTime);
        };

    }



    private StaticMediaFile createStaticMediaFile(String mediaPath, String originalName, String absoluteName) {
        return StaticMediaFile.builder()
                .filePath(mediaPath)
                .extension("tempExtension")
                .originalName(originalName)
                .absoluteName(absoluteName)
                .build();
    }



    private InternetVideoUrl createInternetVideoUrl(String mediaPath, int videoPlayDuration, String videoStartTime) {
        return InternetVideoUrl.builder()
                .filePath(mediaPath)
                .isPlayableVideo(true)
                .videoPlayDuration(videoPlayDuration)
                .videoStartTime(videoStartTime)
                .build();
    }


}
