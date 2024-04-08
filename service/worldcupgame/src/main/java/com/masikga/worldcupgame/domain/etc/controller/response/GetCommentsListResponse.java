package com.masikga.worldcupgame.domain.etc.controller.response;

import com.masikga.worldcupgame.domain.etc.model.Comment;

import java.time.LocalDateTime;

public record GetCommentsListResponse(
        Long commentId,
        Long commentWriterId,
        String writerNickname,
        String body,
        LocalDateTime createdAt
) {

    public static GetCommentsListResponse fromEntity(Comment comment) {

        return new GetCommentsListResponse(
                comment.getId(),
                comment.getMemberId(),
                comment.getNickname(),
                comment.getBody(),
                comment.getCreatedAt()
        );
    }
}
