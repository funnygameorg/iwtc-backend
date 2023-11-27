package com.example.demo.domain.worldcup.controller.request;

import com.example.demo.domain.worldcup.controller.validator.worldcupcontents.VerifyWorldCupContentsName;
import com.example.demo.domain.worldcup.model.vo.VisibleType;

public record UpdateWorldCupContentsRequest (

        @VerifyWorldCupContentsName
        String contentsName,
        String originalName,
        String mediaData,
        String videoStartTime,
        Integer videoPlayDuration,
        VisibleType visibleType
) {}
