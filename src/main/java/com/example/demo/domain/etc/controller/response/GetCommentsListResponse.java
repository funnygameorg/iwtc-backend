package com.example.demo.domain.etc.controller.response;

import com.example.demo.domain.etc.model.Comment;
import com.example.demo.domain.worldcup.controller.response.GetMyWorldCupResponse;

import java.time.LocalDateTime;

public record GetCommentsListResponse(
    Long commentId,
    Long commentWriterId,
    String body,
    LocalDateTime createdAt
    ) {

    public static GetCommentsListResponse fromEntity(Comment comment) {
        return new GetCommentsListResponse(
                comment.getId(),
                comment.getMember().getId(),
                comment.getBody(),
                comment.getCreatedAt()
        );
    }
}
