package com.masikga.worldcupgame.domain.etc.model;

import com.masikga.worldcupgame.common.jpa.MemberTimeBaseEntity;
import com.masikga.worldcupgame.domain.worldcup.model.WorldCupGame;
import com.masikga.worldcupgame.domain.worldcup.model.WorldCupGameContents;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import java.util.Objects;

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
@Where(clause = "soft_delete = false")
@Table(name = "comment")
public class Comment extends MemberTimeBaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(length = 50)
    @org.hibernate.annotations.Comment("댓글 내용")
    private String body;

    @Column(length = 255)
    @org.hibernate.annotations.Comment("댓글 작성에 사용된 닉네임")
    private String nickname;

    @org.hibernate.annotations.Comment("소프트 딜리트 여부")
    private boolean softDelete = false;

    @org.hibernate.annotations.Comment("연결된 월드컵 게임")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(foreignKey = @ForeignKey(NO_CONSTRAINT))
    private WorldCupGame worldCupGame;

    @org.hibernate.annotations.Comment("연결된 월드컵 게임 컨텐츠")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(foreignKey = @ForeignKey(NO_CONSTRAINT))
    private WorldCupGameContents contents;

    @org.hibernate.annotations.Comment("댓글을 작성한 사용자")
    @Column(name = "member_id")
    private Long memberId;

    public void softDelete() {
        this.softDelete = true;
    }

    public boolean isOwner(Long memberId) {
        return Objects.equals(memberId, memberId);
    }

    public static Comment writeWorldCupGameResult(
            WorldCupGame worldCupGame,
            WorldCupGameContents worldCupGameContents,
            Long memberId,
            String body,
            String nickname) {

        return Comment.builder()
                .worldCupGame(worldCupGame)
                .contents(worldCupGameContents)
                .body(body)
                .nickname(nickname)
                .memberId(memberId)
                .build();

    }

}
