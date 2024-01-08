package com.masikga.itwc.domain.etc.model;

import static jakarta.persistence.EnumType.*;
import static java.util.Objects.*;
import static lombok.AccessLevel.*;

import org.hibernate.Hibernate;
import org.hibernate.annotations.Comment;

import com.google.common.base.Objects;
import com.masikga.itwc.common.error.exception.NotNullArgumentException;
import com.masikga.itwc.domain.etc.exception.NotSupportedFileExtensionException;
import com.masikga.itwc.domain.etc.model.vo.FileType;
import com.masikga.itwc.domain.etc.model.vo.MediaFileExtension;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "static_media_file")
@NoArgsConstructor(access = PROTECTED)
public class StaticMediaFile extends MediaFile {

	@Comment("파일의 원래 이름")
	@NotNull
	@NotBlank
	private String originalName;

	// TODO : MediaFile(부모 클래스)로 이동하기
	@Comment("파일 확장자")
	@NotNull
	@Enumerated(STRING)
	private MediaFileExtension extension;

	public void update(String objectKey, String originalName, String detailFileType) {

		if (isNull(objectKey) || isNull(originalName) || isNull(detailFileType)) {
			throw new NotNullArgumentException(objectKey, originalName);
		}

		if (!MediaFileExtension.isSupportedType(detailFileType)) {
			throw new NotSupportedFileExtensionException(detailFileType);
		}

		super.objectKey = objectKey;
		this.originalName = originalName;
		this.extension = MediaFileExtension.valueOf(detailFileType);
	}

	@Builder
	public StaticMediaFile(String originalName, String extension, Long id, String objectKey, String bucketName) {
		super(id, objectKey, FileType.STATIC_MEDIA_FILE, bucketName);
		this.originalName = originalName;
		this.extension = MediaFileExtension.valueOf(extension);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
			return false;
		StaticMediaFile other = (StaticMediaFile)o;

		return Objects.equal(objectKey, other.getObjectKey())
			&& Objects.equal(getObjectKey(), other.getObjectKey())
			&& Objects.equal(extension, other.getExtension());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(objectKey, getObjectKey(), extension);

	}
}
