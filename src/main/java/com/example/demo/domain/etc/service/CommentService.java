package com.example.demo.domain.etc.service;

import com.example.demo.domain.etc.controller.request.WriteCommentRequest;
import com.example.demo.domain.etc.controller.response.GetCommentsListResponse;
import com.example.demo.domain.etc.model.Comment;
import com.example.demo.domain.etc.repository.CommentRepository;
import com.example.demo.domain.member.repository.MemberRepository;
import com.example.demo.domain.worldcup.exception.NotFoundWorldCupContentsException;
import com.example.demo.domain.worldcup.exception.NotFoundWorldCupGameException;
import com.example.demo.domain.worldcup.model.WorldCupGame;
import com.example.demo.domain.worldcup.repository.WorldCupGameContentsRepository;
import com.example.demo.domain.worldcup.repository.WorldCupGameRepository;
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

        var writer = memberRepository.findById(memberId).orElse(null);

        var newComment = Comment.writeWorldCupGameResult(
                worldCupGame,
                worldCupGameContents,
                writer,
                request.body(),
                request.nickname()
        );

        commentRepository.save(newComment);

    }
}
