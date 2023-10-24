package com.example.demo.domain.worldcup.model.entity;

import com.example.demo.common.jpa.TimeBaseEntity;
import com.example.demo.domain.worldcup.model.entity.vo.VisibleType;
import com.example.demo.domain.worldcup.model.entity.vo.WorldCupGameRound;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@Builder
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
@Table(name = "WORLD_CUP_GAME", uniqueConstraints = {
        @UniqueConstraint(name = "title_unique", columnNames = "title")
})
public class WorldCupGame extends TimeBaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    private String title;

    private String description;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private WorldCupGameRound round;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private VisibleType visibleType;

    private long views;

    private boolean softDelete;

    private long memberId;



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
            return false;
        WorldCupGame worldCupGame = (WorldCupGame) o;
        return Objects.equals(title, worldCupGame.getTitle());
    }

    @Override
    public int hashCode() {
        return title.hashCode();
    }

}
