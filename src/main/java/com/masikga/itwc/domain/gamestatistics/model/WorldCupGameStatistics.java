package com.masikga.itwc.domain.gamestatistics.model;

import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import com.masikga.itwc.common.jpa.MemberTimeBaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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