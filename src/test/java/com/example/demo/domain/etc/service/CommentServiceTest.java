package com.example.demo.domain.etc.service;

import static com.example.demo.domain.worldcup.model.vo.VisibleType.*;
import static com.example.demo.helper.TestConstant.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.domain.etc.controller.request.WriteCommentRequest;
import com.example.demo.domain.etc.exception.NotFoundCommentException;
import com.example.demo.domain.etc.exception.NotOwnerCommentException;
import com.example.demo.domain.etc.model.Comment;
import com.example.demo.domain.etc.model.StaticMediaFile;
import com.example.demo.domain.etc.repository.CommentRepository;
import com.example.demo.domain.etc.repository.MediaFileRepository;
import com.example.demo.domain.member.model.Member;
import com.example.demo.domain.member.repository.MemberRepository;
import com.example.demo.domain.worldcup.exception.NotFoundWorldCupContentsException;
import com.example.demo.domain.worldcup.exception.NotFoundWorldCupGameException;
import com.example.demo.domain.worldcup.model.WorldCupGame;
import com.example.demo.domain.worldcup.model.WorldCupGameContents;
import com.example.demo.domain.worldcup.repository.WorldCupGameContentsRepository;
import com.example.demo.domain.worldcup.repository.WorldCupGameRepository;
import com.example.demo.helper.DataBaseCleanUp;
import com.example.demo.helper.testbase.IntegrationBaseTest;

public class CommentServiceTest implements IntegrationBaseTest {

	@Autowired
	private WorldCupGameRepository worldCupGameRepository;
	@Autowired
	private MemberRepository memberRepository;
	@Autowired
	private WorldCupGameContentsRepository worldCupGameContentsRepository;
	@Autowired
	private MediaFileRepository mediaFileRepository;
	@Autowired
	private CommentRepository commentRepository;

	@Autowired
	private CommentService commentService;
	@Autowired
	private DataBaseCleanUp dataBaseCleanUp;

	@AfterEach
	public void tearDown() {
		dataBaseCleanUp.truncateAllEntity();
	}

	@Nested
	@DisplayName("월드컵의 댓글을 조회할 수 있다.")
	class GetComments {

		@Test
		@DisplayName(SUCCESS_PREFIX + "1페이지 코멘트 2개 조회")
		public void success1() {

			// given
			var worldCupGame = WorldCupGame.builder()
				.title("title 1")
				.memberId(1L)
				.description("desc1")
				.visibleType(PUBLIC)
				.build();

			var member = Member.builder()
				.nickname("Aawfawf")
				.password("Afwegw")
				.serviceId("Awegwegweg")
				.build();

			var mediaFile = StaticMediaFile.builder()
				.originalName("a")
				.bucketName("a")
				.objectKey("a")
				.extension("A")
				.build();
			var worldCupGameContents = WorldCupGameContents.builder()
				.name("name 1")
				.worldCupGame(worldCupGame)
				.mediaFile(mediaFile)
				.build();

			var comment1 = Comment.builder()
				.body("코멘트 1")
				.member(member)
				.worldCupGame(worldCupGame)
				.nickname("Aawfawf")
				.contents(worldCupGameContents)
				.build();
			var comment2 = Comment.builder()
				.body("코멘트 2")
				.member(member)
				.nickname("Aawfawf")
				.worldCupGame(worldCupGame)
				.contents(worldCupGameContents)
				.build();

			memberRepository.save(member);
			worldCupGameRepository.save(worldCupGame);
			mediaFileRepository.save(mediaFile);
			worldCupGameContentsRepository.save(worldCupGameContents);

			var savedCommentList = commentRepository.saveAll(List.of(comment1, comment2));

			// when
			var response = commentService.getComments(1L, 0);

			// then
			assertAll(
				() -> assertThat(response.size()).isEqualTo(2),
				() -> assertThat(response.get(0).commentId()).isEqualTo(1),
				() -> assertThat(response.get(0).commentWriterId()).isEqualTo(1),
				() -> assertThat(response.get(0).writerNickname()).isEqualTo("Aawfawf"),
				() -> assertThat(response.get(0).body()).isEqualTo("코멘트 1"),
				() -> assertThat(response.get(0).createdAt()).isEqualTo(savedCommentList.get(0).getCreatedAt()),

				() -> assertThat(response.get(1).commentId()).isEqualTo(2),
				() -> assertThat(response.get(1).commentWriterId()).isEqualTo(1),
				() -> assertThat(response.get(0).writerNickname()).isEqualTo("Aawfawf"),
				() -> assertThat(response.get(1).body()).isEqualTo("코멘트 2"),
				() -> assertThat(response.get(1).createdAt()).isEqualTo(savedCommentList.get(1).getCreatedAt())
			);

		}

		@Test
		@DisplayName(SUCCESS_PREFIX + "2페이지 코멘트 3개 조회")
		public void success2() {

			// given
			var worldCupGame = WorldCupGame.builder()
				.title("title 1")
				.memberId(1L)
				.description("desc1")
				.visibleType(PUBLIC)
				.build();

			var member = Member.builder()
				.nickname("Aawfawf")
				.password("Afwegw")
				.serviceId("Awegwegweg")
				.build();

			var mediaFile = StaticMediaFile.builder()
				.originalName("a")
				.bucketName("a")
				.objectKey("a")
				.extension("A")
				.build();

			var worldCupGameContents = WorldCupGameContents.builder()
				.name("name 1")
				.worldCupGame(worldCupGame)
				.mediaFile(mediaFile)
				.build();

			var commentList = IntStream.range(1, 19)
				.mapToObj(idx ->
					Comment.builder()
						.body("코멘트 " + idx)
						.member(member)
						.nickname("Aawfawf")
						.worldCupGame(worldCupGame)
						.contents(worldCupGameContents)
						.build()
				).toList();

			memberRepository.save(member);
			worldCupGameRepository.save(worldCupGame);
			mediaFileRepository.save(mediaFile);
			worldCupGameContentsRepository.save(worldCupGameContents);

			var savedCommentList = commentRepository.saveAll(commentList);

			// when
			var response = commentService.getComments(1L, 1);

			// then : 2 페이지의 첫 요소, 마지막 요소 검증
			assertAll(
				() -> assertThat(response.size()).isEqualTo(3),
				() -> assertThat(response.get(0).commentId()).isEqualTo(16),
				() -> assertThat(response.get(0).commentWriterId()).isEqualTo(1),
				() -> assertThat(response.get(0).writerNickname()).isEqualTo("Aawfawf"),
				() -> assertThat(response.get(0).body()).isEqualTo("코멘트 16"),
				() -> assertThat(response.get(0).createdAt()).isEqualTo(savedCommentList.get(15).getCreatedAt()),

				() -> assertThat(response.get(2).commentId()).isEqualTo(18),
				() -> assertThat(response.get(2).commentWriterId()).isEqualTo(1),
				() -> assertThat(response.get(2).writerNickname()).isEqualTo("Aawfawf"),
				() -> assertThat(response.get(2).body()).isEqualTo("코멘트 18"),
				() -> assertThat(response.get(2).createdAt()).isEqualTo(savedCommentList.get(17).getCreatedAt())
			);

		}

		@Test
		@DisplayName(SUCCESS_PREFIX + "2페이지 코멘트 0개 조회")
		public void success3() {

			// given
			var worldCupGame = WorldCupGame.builder()
				.title("title 1")
				.memberId(1L)
				.description("desc1")
				.visibleType(PUBLIC)
				.build();

			var member = Member.builder()
				.nickname("Aawfawf")
				.password("Afwegw")
				.serviceId("Awegwegweg")
				.build();

			var mediaFile = StaticMediaFile.builder()
				.originalName("a")
				.bucketName("a")
				.objectKey("a")
				.extension("A")
				.build();
			var worldCupGameContents = WorldCupGameContents.builder()
				.name("name 1")
				.worldCupGame(worldCupGame)
				.mediaFile(mediaFile)
				.build();
			var commentList = IntStream.range(1, 16)
				.mapToObj(idx ->
					Comment.builder()
						.body("코멘트 " + idx)
						.member(member)
						.contents(worldCupGameContents)
						.build()
				).toList();

			memberRepository.save(member);
			worldCupGameRepository.save(worldCupGame);
			mediaFileRepository.save(mediaFile);
			worldCupGameContentsRepository.save(worldCupGameContents);

			var savedCommentList = commentRepository.saveAll(commentList);

			// when
			var response = commentService.getComments(1L, 1);

			// then : 2 페이지의 첫 요소, 마지막 요소 검증
			assertThat(response.size()).isEqualTo(0);

		}

		@Test
		@DisplayName(EXCEPTION_PREFIX + "존재하지 않는 월드컵의 댓글은 조회할 수 없다.")
		public void fail1() {

			// when & then
			assertThrows(
				NotFoundWorldCupGameException.class,
				() -> commentService.getComments(1L, 1)
			);

		}

	}

	@Nested
	@DisplayName("댓글을 작성할 수 있다.")
	class WriteComment {

		@Test
		@DisplayName(SUCCESS_PREFIX)
		public void success1() {

			// given
			var mediaFile = StaticMediaFile.builder()
				.originalName("a")
				.bucketName("a")
				.objectKey("a")
				.extension("A")
				.build();

			var worldCup = WorldCupGame.builder()
				.title("타이틀1")
				.description("디스크립션1")
				.memberId(1)
				.visibleType(PUBLIC)
				.build();

			var contents = WorldCupGameContents.builder()
				.name("콘텐츠명 1")
				.softDelete(false)
				.visibleType(PUBLIC)
				.worldCupGame(worldCup)
				.mediaFile(mediaFile)
				.build();

			var member = Member.builder()
				.serviceId("world-id123")
				.password("world-pass123")
				.nickname("날아라 호박")
				.build();

			var request = WriteCommentRequest.builder()
				.nickname("날아라 호박")
				.body("댓글의 내용이야!")
				.build();

			mediaFileRepository.save(mediaFile);
			memberRepository.save(member);
			worldCupGameRepository.save(worldCup);
			worldCupGameContentsRepository.save(contents);

			// when
			commentService.writeComment(request, 1L, 1L, 1L);

			// then
			var comment = commentRepository.findById(1L).get();

			assertAll(
				() -> assertThat(comment.getBody()).isEqualTo("댓글의 내용이야!"),
				() -> assertThat(comment.getMember().getId()).isEqualTo(1L),
				() -> assertThat(comment.getContents().getId()).isEqualTo(1L),
				() -> assertThat(comment.getNickname()).isEqualTo("날아라 호박")
			);
		}

		@Test
		@DisplayName(EXCEPTION_PREFIX + "존재하지 않는 월드컵에 댓글을 작성할 수 없다.")
		public void fail1() {
			// given
			var request = WriteCommentRequest.builder()
				.nickname("날아라 호박")
				.body("댓글의 내용이야!")
				.build();

			var mediaFile = StaticMediaFile.builder()
				.originalName("a")
				.bucketName("a")
				.objectKey("a")
				.extension("A")
				.build();

			var worldCup = WorldCupGame.builder()
				.title("타이틀1")
				.description("디스크립션1")
				.memberId(1)
				.visibleType(PUBLIC)
				.build();

			var member = Member.builder()
				.serviceId("world-id123")
				.password("world-pass123")
				.nickname("날아라 호박")
				.build();

			mediaFileRepository.save(mediaFile);
			memberRepository.save(member);
			worldCupGameRepository.save(worldCup);

			// when
			assertThrows(
				NotFoundWorldCupContentsException.class,
				() -> commentService.writeComment(request, 1L, 1L, 1L)
			);
		}

		@Test
		@DisplayName(EXCEPTION_PREFIX + "존재하지 않는 월드컵 컨텐츠에 댓글을 작성할 수 없다.")
		public void fail2() {
			// given
			var request = WriteCommentRequest.builder()
				.nickname("날아라 호박")
				.body("댓글의 내용이야!")
				.build();

			// when
			assertThrows(
				NotFoundWorldCupGameException.class,
				() -> commentService.writeComment(request, 1L, 1L, 1L)
			);
		}

	}

	@Nested
	@DisplayName("월드컵 게임의 댓글을 삭제할 수 있다.")
	class deleteComment {

		@Test
		@DisplayName(SUCCESS_PREFIX)
		public void success1() {

			// given
			var mediaFile = StaticMediaFile.builder()
				.originalName("a")
				.bucketName("a")
				.objectKey("a")
				.extension("A")
				.build();

			var worldCup = WorldCupGame.builder()
				.title("타이틀1")
				.description("디스크립션1")
				.memberId(1)
				.visibleType(PUBLIC)
				.build();

			var contents = WorldCupGameContents.builder()
				.name("콘텐츠명 1")
				.softDelete(false)
				.visibleType(PUBLIC)
				.worldCupGame(worldCup)
				.mediaFile(mediaFile)
				.build();

			var member = Member.builder()
				.serviceId("world-id123")
				.password("world-pass123")
				.nickname("날아라 호박")
				.build();

			var comment = Comment.builder()
				.body("내용 1")
				.nickname("제로")
				.softDelete(false)
				.contents(contents)
				.worldCupGame(worldCup)
				.member(member)
				.build();

			memberRepository.save(member);
			mediaFileRepository.save(mediaFile);
			worldCupGameRepository.save(worldCup);
			worldCupGameContentsRepository.save(contents);
			commentRepository.save(comment);

			// when
			commentService.deleteComment(1L, 1L);

			// then
			var deletedComment = commentRepository.findById(1L);

			assertThat(deletedComment.isEmpty()).isTrue();

		}

		@Test
		@DisplayName(EXCEPTION_PREFIX + "존재하지 않는 댓글을 삭제할 수 없다.")
		public void fail1() {

			// when then
			assertThrows(
				NotFoundCommentException.class,
				() -> commentService.deleteComment(1L, 1L)
			);
		}

		@Test
		@DisplayName(EXCEPTION_PREFIX + "자신의 댓글이 아니면 삭제할 수 없다.")
		public void fail2() {

			// given
			var mediaFile = StaticMediaFile.builder()
				.originalName("a")
				.bucketName("a")
				.objectKey("a")
				.extension("A")
				.build();

			var worldCup = WorldCupGame.builder()
				.title("타이틀1")
				.description("디스크립션1")
				.memberId(1)
				.visibleType(PUBLIC)
				.build();

			var contents = WorldCupGameContents.builder()
				.name("콘텐츠명 1")
				.softDelete(false)
				.visibleType(PUBLIC)
				.worldCupGame(worldCup)
				.mediaFile(mediaFile)
				.build();

			var member = Member.builder()
				.serviceId("world-id123")
				.password("world-pass123")
				.nickname("날아라 호박")
				.build();

			var comment = Comment.builder()
				.body("내용 1")
				.nickname("제로")
				.softDelete(false)
				.contents(contents)
				.worldCupGame(worldCup)
				.member(member)
				.build();

			memberRepository.save(member);
			mediaFileRepository.save(mediaFile);
			worldCupGameRepository.save(worldCup);
			worldCupGameContentsRepository.save(contents);
			commentRepository.save(comment);

			// when then
			assertThrows(
				NotOwnerCommentException.class,
				() -> commentService.deleteComment(1L, 2L)
			);

		}

	}

}
