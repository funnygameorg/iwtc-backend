package com.masikga.itwc.domain.etc.model;

import com.masikga.itwc.common.jpa.TimeBaseEntity;
import com.masikga.itwc.domain.etc.model.vo.FileType;
import com.masikga.itwc.domain.etc.model.vo.MediaFileExtension;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.InheritanceType.JOINED;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@Table(name = "media_file")
@AllArgsConstructor(access = PROTECTED)
@NoArgsConstructor(access = PROTECTED)
@Inheritance(strategy = JOINED)
@DiscriminatorColumn(name = "d_type")
public abstract class MediaFile extends TimeBaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Long id;

	@NotNull
	@NotBlank
	@org.hibernate.annotations.Comment("s3 오브젝트 키")
	protected String objectKey;

	@NotNull
	@org.hibernate.annotations.Comment("미디어 파일의 형태 (저장된 파일, 영상 링크...)")
	@Enumerated(value = EnumType.STRING)
	protected FileType fileType;

	@org.hibernate.annotations.Comment("저장 버킷명")
	private String bucket;

	@NotNull
	@Comment("동영상의 형태 (ex : 유튜브 링크, 네이버 비디오 링크...)")
	@Enumerated(STRING)
	protected MediaFileExtension detailType;

	@NotNull
	@org.hibernate.annotations.Comment("원본 파일 크기")
	private String originalFileSize;

}
