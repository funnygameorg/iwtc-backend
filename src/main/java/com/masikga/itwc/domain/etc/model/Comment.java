package com.masikga.itwc.domain.etc.model;

import static jakarta.persistence.ConstraintMode.*;
import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import java.util.Objects;

import org.hibernate.annotations.Where;

import com.masikga.itwc.common.jpa.MemberTimeBaseEntity;
import com.masikga.itwc.domain.member.model.Member;
import com.masikga.itwc.domain.worldcup.model.WorldCupGame;
import com.masikga.itwc.domain.worldcup.model.WorldCupGameContents;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
	@ManyToOne(fetch = LAZY)
	@JoinColumn(foreignKey = @ForeignKey(NO_CONSTRAINT))
	private Member member;

	public void softDelete() {
		this.softDelete = true;
	}

	public boolean isOwner(Long memberId) {
		return Objects.equals(member.getId(), memberId);
	}

	public static Comment writeWorldCupGameResult(
		WorldCupGame worldCupGame,
		WorldCupGameContents worldCupGameContents,
		Member member,
		String body,
		String nickname) {

		return Comment.builder()
			.worldCupGame(worldCupGame)
			.contents(worldCupGameContents)
			.body(body)
			.nickname(nickname)
			.member(member)
			.build();

	}

}
