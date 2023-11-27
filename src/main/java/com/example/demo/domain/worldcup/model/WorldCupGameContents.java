package com.example.demo.domain.worldcup.model;

import com.example.demo.common.jpa.TimeBaseEntity;
import com.example.demo.domain.etc.model.InternetVideoUrl;
import com.example.demo.domain.etc.model.MediaFile;
import com.example.demo.domain.etc.model.StaticMediaFile;
import com.example.demo.domain.worldcup.model.vo.VisibleType;
import com.google.common.base.Objects;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.Where;

import static com.example.demo.domain.etc.model.vo.FileType.*;
import static com.google.common.base.Objects.equal;
import static jakarta.persistence.ConstraintMode.NO_CONSTRAINT;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@Builder
@Where(clause = "SOFT_DELETE = false")
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
@Table(name = "WORLD_CUP_GAME_CONTENTS",
        indexes = {
                // 게임 랭킹 노출용
                @Index(name = "VISIBLE_TYPE__GAME_SCORE__INDEX", columnList = "visibleType, gameScore")
        }
)
public class WorldCupGameContents extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    @Comment("월드컵 컨텐츠 이름")
    private String name;

    @NotNull
    @ManyToOne(fetch = LAZY)
    @Comment("연결된 이상형 월드컵")
    @JoinColumn(foreignKey = @ForeignKey(NO_CONSTRAINT), nullable = false)
    private WorldCupGame worldCupGame;

    @OneToOne(fetch = LAZY)
    @Comment("연결된 미디어 파일")
    @JoinColumn(foreignKey = @ForeignKey(NO_CONSTRAINT), nullable = false)
    private MediaFile mediaFile;

    @Enumerated(value = EnumType.STRING)
    @Comment("컨텐츠 공개 여부")
    private VisibleType visibleType;

    @Comment("월드컵에서의 컨텐츠 순위")
    private int gameRank;

    @Comment("컨텐츠의 승리 점수")
    private int gameScore;

    @Comment("삭제 여부")
    private boolean softDelete = false;



    public void updateByCommonManage(
            String contentsName,
            String originalName,
            String videoStartTime,
            Integer videoPlayDuration,
            VisibleType visibleType,
            String objectKey
    ) {

        this.name = contentsName;
        this.visibleType = visibleType;

        mediaFile = (MediaFile) Hibernate.unproxy(mediaFile);

        if(mediaFile instanceof StaticMediaFile) {

            StaticMediaFile staticMediaFile = (StaticMediaFile) mediaFile;
            staticMediaFile.update(objectKey, originalName);

        } else {

            InternetVideoUrl internetVideoUrl = (InternetVideoUrl) mediaFile;
            internetVideoUrl.update(objectKey, videoStartTime, videoPlayDuration);

        }

    }





    public static WorldCupGameContents createNewContents(
            WorldCupGame worldCupGame,
            MediaFile mediaFile,
            String contentsName,
            VisibleType visibleType
    ) {
        return WorldCupGameContents.builder()
                .name(contentsName)
                .worldCupGame(worldCupGame)
                .mediaFile(mediaFile)
                .visibleType(visibleType)
                .gameRank(0)
                .gameScore(0)
                .build();
    }



    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
            return false;
        WorldCupGameContents other = (WorldCupGameContents) o;
        return equal(name, other.getName());
    }




    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    public void softDelete() {
        this.softDelete = true;
    }
}
