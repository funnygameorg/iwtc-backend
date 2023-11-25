package com.example.demo.domain.etc.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import static com.example.demo.domain.etc.model.vo.FileType.INTERNET_VIDEO_URL;
import static lombok.AccessLevel.PROTECTED;

/**
 *  startTime example
 *      1. 00001 : 000분 01초
 *      2. 00101 : 001분 01초
 *      3. 02000 : 020분 00초
 *      4. 00113 : 001분 13초
 */
@Getter
@Entity
@Table(name = "INTERNET_VIDEO_URL")
@NoArgsConstructor(access = PROTECTED)
public class InternetVideoUrl extends MediaFile {
    @Comment("현재 실행가능한 영상인가?") // 스케쥴러로 비정상 컨텐츠 표시읽고 사용자 알림으로 알려주기
    private boolean isPlayableVideo;

    @Comment("영상 시작 시간, 0부터 9까지 이루어진 5글자의 숫자만 허용, 앞 3자리 숫자는 '분'을 표현하며 뒤 2자리는 '초'를 표현")
    @Pattern(regexp = "\\d{5}")
    private String videoStartTime;

    @Comment("영상 반복 구간, 2부터 5까지 이루어진 1글자의 숫자만 허용하며 단위는 초")
    @Min(value = 2, message = "영상 재생 시간은 2 ~ 5초 입니다.")
    @Max(value = 5, message = "영상 재생 시간은 2 ~ 5초 입니다.")
    private int videoPlayDuration;

    @Builder
    private InternetVideoUrl(Long id, String filePath, boolean isPlayableVideo, String videoStartTime, int videoPlayDuration, String bucketName) {
        super(id, filePath, INTERNET_VIDEO_URL, bucketName);
        this.isPlayableVideo = isPlayableVideo;
        this.videoStartTime = videoStartTime;
        this.videoPlayDuration = videoPlayDuration;
    }
}
