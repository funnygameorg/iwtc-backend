package com.masikga.itwc.domain.etc.controller.response;

import java.time.LocalDateTime;

import com.masikga.itwc.domain.etc.model.Comment;

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
