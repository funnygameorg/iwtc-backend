package com.masikga.worldcupgame.domain.etc.service;

import com.masikga.member.exception.NotFoundMemberExceptionMember;
import com.masikga.member.model.Member;
import com.masikga.member.repository.MemberRepository;
import com.masikga.worldcupgame.domain.etc.controller.request.WriteCommentRequest;
import com.masikga.worldcupgame.domain.etc.controller.response.GetCommentsListResponse;
import com.masikga.worldcupgame.domain.etc.exception.NotFoundCommentExceptionMember;
import com.masikga.worldcupgame.domain.etc.exception.NotOwnerCommentExceptionMember;
import com.masikga.worldcupgame.domain.etc.model.Comment;
import com.masikga.worldcupgame.domain.etc.repository.CommentRepository;
import com.masikga.worldcupgame.domain.worldcup.exception.NotFoundWorldCupContentsExceptionMember;
import com.masikga.worldcupgame.domain.worldcup.exception.NotFoundWorldCupGameExceptionMember;
import com.masikga.worldcupgame.domain.worldcup.repository.WorldCupGameContentsRepository;
import com.masikga.worldcupgame.domain.worldcup.repository.WorldCupGameRepository;
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
                .orElseThrow(() -> new NotFoundWorldCupGameExceptionMember(worldCupId));

        var pageRequest = PageRequest.of(offset, 15);

        var commentsList = commentRepository.findAllByWorldCupGame(worldCupGame, pageRequest);

        return commentsList.stream()
                .map(GetCommentsListResponse::fromEntity)
                .toList();

    }

    @Transactional
    public void writeComment(WriteCommentRequest request, Long memberId, Long worldCupId, Long contentsId) {

        var worldCupGame = worldCupGameRepository.findById(worldCupId)
                .orElseThrow(() -> new NotFoundWorldCupGameExceptionMember(worldCupId));

        var worldCupGameContents = worldCupGameContentsRepository.findById(contentsId)
                .orElseThrow(() -> new NotFoundWorldCupContentsExceptionMember(contentsId));

        Member writer = null;

        if (memberId != null) {
            writer = memberRepository.findById(memberId)
                    .orElseThrow(NotFoundMemberExceptionMember::new);
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
                .orElseThrow(() -> new NotFoundCommentExceptionMember(commentId));

        if (!comment.isOwner(memberId)) {
            throw new NotOwnerCommentExceptionMember(memberId);
        }

        comment.softDelete();

    }

}
