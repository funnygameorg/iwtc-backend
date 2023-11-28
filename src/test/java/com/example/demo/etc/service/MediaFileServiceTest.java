package com.example.demo.etc.service;

import com.example.demo.domain.etc.controller.response.MediaFileResponse;
import com.example.demo.domain.etc.model.StaticMediaFile;
import com.example.demo.domain.etc.repository.MediaFileRepository;
import com.example.demo.domain.etc.service.MediaFileService;
import com.example.demo.helper.DataBaseCleanUp;
import com.example.demo.helper.testbase.IntegrationBaseTest;
import com.example.demo.infra.s3.S3Component;
import org.junit.jupiter.api.*;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.IOException;

import static com.example.demo.domain.etc.model.vo.FileType.STATIC_MEDIA_FILE;
import static com.example.demo.helper.TestConstant.SUCCESS_PREFIX;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

public class MediaFileServiceTest implements IntegrationBaseTest {

    @Autowired
    private MediaFileService mediaFileService;

    @Autowired
    private MediaFileRepository mediaFileRepository;

    @MockBean
    private S3Component s3Component;

    @Autowired
    private DataBaseCleanUp dataBaseCleanUp;


    @BeforeEach
    public void setUp() {
        dataBaseCleanUp.truncateAllEntity();
    }




    @Nested
    @DisplayName("미디어 파일 1건을 조회할 수 있다.")
    class getMediaFile {

        @Test
        @DisplayName(SUCCESS_PREFIX)
        public void success() throws IOException {

            // given
            StaticMediaFile staticMediaFile = StaticMediaFile.builder()
                    .objectKey("A-46")
                    .bucketName("bucketA")
                    .originalName("originalNameA")
                    .extension(".png")
                    .build();

            mediaFileRepository.save(staticMediaFile);

            given(s3Component.getObject("A-46"))
                    .willReturn("dataA");


            // when
            MediaFileResponse response = mediaFileService.getMediaFile(1L);

            //then
            assertAll(
                    () -> assertThat(response.mediaFileId()).isEqualTo(1),
                    () -> assertThat(response.originalName()).isEqualTo("originalNameA"),
                    () -> assertThat(response.mediaData()).isEqualTo("dataA"),
                    () -> assertThat(response.fileType()).isEqualTo(STATIC_MEDIA_FILE),
                    () -> assertThat(response.videoPlayDuration()).isNull(),
                    () -> assertThat(response.videoStartTime()).isNull()
            );

        }
    }





}
