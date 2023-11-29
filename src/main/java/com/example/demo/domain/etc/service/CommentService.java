package com.example.demo.domain.etc.service;

import com.example.demo.domain.etc.controller.response.GetCommentsListResponse;
import com.example.demo.domain.etc.repository.CommentRepository;
import com.example.demo.domain.worldcup.exception.NotFoundWorldCupGameException;
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

    public List<GetCommentsListResponse> getComments(Long worldCupId, Integer offset) {

        var worldCupGame = worldCupGameRepository.findById(worldCupId)
                .orElseThrow(() -> new NotFoundWorldCupGameException(worldCupId));

        var pageRequest = PageRequest.of(offset, 15);

        var commentsList = commentRepository.findAllByWorldCupGame(worldCupGame, pageRequest);

        return commentsList.stream()
                .map(GetCommentsListResponse::fromEntity)
                .toList();

    }



}
