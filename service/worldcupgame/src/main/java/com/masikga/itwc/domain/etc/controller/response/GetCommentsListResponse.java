package com.masikga.itwc.domain.etc.controller.response;

import com.masikga.itwc.domain.etc.model.Comment;

import java.time.LocalDateTime;

public record GetCommentsListResponse(
	Long commentId,
	Long commentWriterId,
	String writerNickname,
	String body,
	LocalDateTime createdAt
) {

	public static GetCommentsListResponse fromEntity(Comment comment) {

		var nullableWriterId = comment.getMember() != null ? comment.getMember().getId() : null;

		return new GetCommentsListResponse(
			comment.getId(),
			nullableWriterId,
			comment.getNickname(),
			comment.getBody(),
			comment.getCreatedAt()
		);
	}
}
