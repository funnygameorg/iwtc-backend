package com.example.demo.domain.worldcup.model;

import com.example.demo.common.jpa.MemberTimeBaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

/**
 * 게임 통계 데이터를 표현한다.
 */
@Getter
@Entity
@Builder
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
public class WorldCupGameStatistics extends MemberTimeBaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

}