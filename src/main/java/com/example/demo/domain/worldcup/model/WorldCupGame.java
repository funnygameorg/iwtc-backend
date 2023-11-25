package com.example.demo.domain.worldcup.model;

import com.example.demo.common.jpa.TimeBaseEntity;
import com.example.demo.domain.worldcup.model.vo.VisibleType;
import com.example.demo.domain.worldcup.repository.projection.GetWorldCupGamePageProjection;
import com.example.demo.domain.gamestatistics.model.WorldCupGameStatistics;
import com.google.common.base.Objects;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Comment;

import static jakarta.persistence.ConstraintMode.NO_CONSTRAINT;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@SqlResultSetMapping(
        name = "FindWorldCupGamePageProjectionMapping",
        classes = {
                @ConstructorResult(
                        targetClass = GetWorldCupGamePageProjection.class,
                        columns = {
                                @ColumnResult(name = "id", type = Long.class),
                                @ColumnResult(name = "title", type = String.class),
                                @ColumnResult(name = "description", type = String.class),
                                @ColumnResult(name = "contentsName1", type = String.class),
                                @ColumnResult(name = "mediaFileId1", type = Long.class),
                                @ColumnResult(name = "contentsName2", type = String.class),
                                @ColumnResult(name = "mediaFileId2", type = Long.class)
                        }
                )
        }
)
@Getter
@Entity
@Builder
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
@Table(name = "WORLD_CUP_GAME",
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
    @Comment("월드컵 이름")
    private String title;

    @Size(max = 100, message = "description 최대 100자")
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

    @Comment("해당 월드컵 통계")
    @OneToOne(fetch = LAZY)
    @JoinColumn(foreignKey = @ForeignKey(NO_CONSTRAINT))
    private WorldCupGameStatistics worldCupGameStatistics;

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
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
            return false;
        WorldCupGame other = (WorldCupGame) o;

        return Objects.equal(title, other.getTitle());
    }



    @Override
    public int hashCode() {
        return Objects.hashCode(title);
    }




}
