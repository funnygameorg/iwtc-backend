package com.example.demo.domain.worldcup.controller.request;

import com.example.demo.domain.etc.model.vo.FileType;
import com.example.demo.domain.worldcup.model.WorldCupGame;
import com.example.demo.domain.worldcup.model.WorldCupGameContents;
import com.example.demo.domain.worldcup.model.vo.VisibleType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.ToString;

import java.util.List;

import static com.example.demo.domain.worldcup.model.vo.VisibleType.PUBLIC;

@Builder
@Schema
public record CreateWorldCupContentsRequest (

        List<CreateContentsRequest> data

) {


    @Builder
    @Schema
    public record CreateContentsRequest(
            @Schema(description = "월드컵 컨텐츠 이름")
            @NotBlank(message = "월드컵 컨텐츠 이름: 필수 값")
            String contentsName,

            VisibleType visibleType,

            CreateMediaFileRequest createMediaFileRequest
    ) {}



    @Builder
    @Schema
    public record CreateMediaFileRequest(

            FileType fileType,

            @Schema(description = "월드컵 컨텐츠 파일명")
            @NotBlank(message = "월드컵 컨텐츠 파일 이름: 필수 값")
            String mediaData,

            // STATIC FILE TYPE
            String originalName,

            // MOVIE URL TYPE
            String videoStartTime,

            Integer videoPlayDuration

    ) { }



}
