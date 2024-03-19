package com.masikga.worldcupgame.domain.worldcup.model;

import com.google.common.base.Objects;
import com.masikga.worldcupgame.common.jpa.TimeBaseEntity;
import com.masikga.worldcupgame.domain.worldcup.model.vo.VisibleType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.Where;

import static jakarta.persistence.EnumType.STRING;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@Builder
@Where(clause = "SOFT_DELETE = false")
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
@Table(name = "world_cup_game",
        uniqueConstraints = {
                @UniqueConstraint(name = "TITLE__UNIQUE", columnNames = "title")
        },
        indexes = {
                // 메인 페이지 노출용
                @Index(name = "VIEWS__INDEX", columnList = "views")
        }
)
public class WorldCupGame extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    @Column(length = 35)
    @Comment("월드컵 이름")
    private String title;

    @Size(max = 100, message = "description 최대 100자")
    @Column(length = 100)
    @Comment("월드컵 설명, 최대 100자")
    private String description;

    @Comment("월드컵 공개 여부")
    @NotNull
    @Enumerated(value = STRING)
    private VisibleType visibleType;

    @Comment("조회수")
    private long views;

    @Comment("삭제 여부")
    private boolean softDelete;

    @Comment("게임 작성자")
    private long memberId;

    public boolean isOwner(long memberId) {
        return memberId == this.memberId;
    }

    // [월드컵 생성, 수정] 페이지에서 작성한 내용을 반영한다.
    public void simpleUpdate(String title, String description, VisibleType visibleType) {
        this.title = title;
        this.description = description;
        this.visibleType = visibleType;
    }

    public static WorldCupGame createNewGame(String title, String description, VisibleType visibleType, long memberId) {
        return WorldCupGame.builder()
                .title(title)
                .description(description)
                .visibleType(visibleType)
                .memberId(memberId)
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
            return false;
        WorldCupGame other = (WorldCupGame) o;

        return Objects.equal(title, other.getTitle());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(title);
    }

    public void softDelete() {
        this.softDelete = true;
    }

}
