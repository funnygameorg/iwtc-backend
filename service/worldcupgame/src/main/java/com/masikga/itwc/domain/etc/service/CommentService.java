package com.masikga.itwc.domain.etc.service;

import com.masikga.itwc.domain.etc.controller.request.WriteCommentRequest;
import com.masikga.itwc.domain.etc.controller.response.GetCommentsListResponse;
import com.masikga.itwc.domain.etc.exception.NotFoundCommentException;
import com.masikga.itwc.domain.etc.exception.NotOwnerCommentException;
import com.masikga.itwc.domain.etc.model.Comment;
import com.masikga.itwc.domain.etc.repository.CommentRepository;
import com.masikga.itwc.domain.member.exception.NotFoundMemberException;
import com.masikga.itwc.domain.member.model.Member;
import com.masikga.itwc.domain.member.repository.MemberRepository;
import com.masikga.itwc.domain.worldcup.exception.NotFoundWorldCupContentsException;
import com.masikga.itwc.domain.worldcup.exception.NotFoundWorldCupGameException;
import com.masikga.itwc.domain.worldcup.repository.WorldCupGameContentsRepository;
import com.masikga.itwc.domain.worldcup.repository.WorldCupGameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

	private final CommentRepository commentRepository;
	private final WorldCupGameRepository worldCupGameRepository;
	private final WorldCupGameContentsRepository worldCupGameContentsRepository;
	private final MemberRepository memberRepository;

	public List<GetCommentsListResponse> getComments(Long worldCupId, Integer offset) {

		var worldCupGame = worldCupGameRepository.findById(worldCupId)
			.orElseThrow(() -> new NotFoundWorldCupGameException(worldCupId));

		var pageRequest = PageRequest.of(offset, 15);

		var commentsList = commentRepository.findAllByWorldCupGame(worldCupGame, pageRequest);

		return commentsList.stream()
			.map(GetCommentsListResponse::fromEntity)
			.toList();

	}

	@Transactional
	public void writeComment(WriteCommentRequest request, Long memberId, Long worldCupId, Long contentsId) {

		var worldCupGame = worldCupGameRepository.findById(worldCupId)
			.orElseThrow(() -> new NotFoundWorldCupGameException(worldCupId));

		var worldCupGameContents = worldCupGameContentsRepository.findById(contentsId)
			.orElseThrow(() -> new NotFoundWorldCupContentsException(contentsId));

		Member writer = null;

		if (memberId != null) {
			writer = memberRepository.findById(memberId)
				.orElseThrow(NotFoundMemberException::new);
		}

		var newComment = Comment.writeWorldCupGameResult(
			worldCupGame,
			worldCupGameContents,
			writer,
			request.body(),
			request.nickname()
		);

		commentRepository.save(newComment);

	}

	@Transactional
	public void deleteComment(Long commentId, Long memberId) {

		var comment = commentRepository.findById(commentId)
			.orElseThrow(() -> new NotFoundCommentException(commentId));

		if (!comment.isOwner(memberId)) {
			throw new NotOwnerCommentException(memberId);
		}

		comment.softDelete();

	}

}
