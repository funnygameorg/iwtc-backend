package com.example.demo.domain.etc.model;

import com.example.demo.common.jpa.MemberTimeBaseEntity;
import com.example.demo.domain.member.model.Member;
import com.example.demo.domain.worldcup.model.WorldCupGame;
import com.example.demo.domain.worldcup.model.WorldCupGameContents;
import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.ConstraintMode.NO_CONSTRAINT;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PRIVATE)
@Table(name = "COMMENT")
public class Comment extends MemberTimeBaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String body;

    private String nickname;

    private boolean softDelete = false;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(foreignKey = @ForeignKey(NO_CONSTRAINT))
    private WorldCupGame worldCupGame;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(foreignKey = @ForeignKey(NO_CONSTRAINT))
    private WorldCupGameContents contents;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(foreignKey = @ForeignKey(NO_CONSTRAINT))
    private Member member;




    public static Comment writeWorldCupGameResult(
            WorldCupGame worldCupGame,
            WorldCupGameContents worldCupGameContents,
            Member member,
            String body,
            String nickname)
    {

        return Comment.builder()
                .worldCupGame(worldCupGame)
                .contents(worldCupGameContents)
                .body(body)
                .nickname(nickname)
                .member(member)
                .build();

    }



}
