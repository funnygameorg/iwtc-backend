package com.example.demo.domain.worldcup.model;

import com.example.demo.common.jpa.TimeBaseEntity;
import com.example.demo.domain.etc.model.MediaFile;
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

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@Builder
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
@Table(
        name = "WORLD_CUP_GAME_CONTENTS",
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
    private String name;

    @NotNull
    @ManyToOne(fetch = LAZY)
    private WorldCupGame worldCupGame;

    @NotNull
    @OneToOne(fetch = LAZY)
    private MediaFile mediaFile;

    private int gameRank;
    private int gameScore;

    private VisibleType visibleType;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
            return false;
        WorldCupGameContents other = (WorldCupGameContents) o;
        return Objects.equal(name, other.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
