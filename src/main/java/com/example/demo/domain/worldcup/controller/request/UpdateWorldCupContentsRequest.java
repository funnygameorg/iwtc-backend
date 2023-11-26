package com.example.demo.domain.worldcup.controller.request;

import com.example.demo.domain.worldcup.model.vo.VisibleType;

public record UpdateWorldCupContentsRequest (
        String contentsName,
        String originalName,
        String mediaData,
        String videoStartTime,
        Integer videoPlayDuration,
        VisibleType visibleType
) {}
