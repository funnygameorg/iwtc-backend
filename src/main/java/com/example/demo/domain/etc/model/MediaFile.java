package com.example.demo.domain.etc.model;

import static jakarta.persistence.InheritanceType.*;
import static lombok.AccessLevel.*;

import com.example.demo.common.jpa.TimeBaseEntity;
import com.example.demo.domain.etc.model.vo.FileType;

import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

}
