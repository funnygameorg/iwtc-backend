package com.masikga.worldcupgame.domain.etc.model;

import com.google.common.base.Objects;
import com.masikga.error.exception.NotNullArgumentExceptionMember;
import com.masikga.worldcupgame.domain.etc.exception.NotSupportedFileExtensionExceptionMember;
import com.masikga.worldcupgame.domain.etc.model.vo.FileType;
import com.masikga.worldcupgame.domain.etc.model.vo.MediaFileExtension;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Comment;

import static java.util.Objects.isNull;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@Table(name = "static_media_file")
@NoArgsConstructor(access = PROTECTED)
public class StaticMediaFile extends MediaFile {

    @Comment("파일의 원래 이름")
    @NotNull
    @NotBlank
    private String originalName;

    public void update(String objectKey, String originalName, String detailFileType) {

        if (isNull(objectKey) || isNull(originalName) || isNull(detailFileType)) {
            throw new NotNullArgumentExceptionMember(objectKey, originalName);
        }

        if (!MediaFileExtension.isSupportedType(detailFileType)) {
            throw new NotSupportedFileExtensionExceptionMember(detailFileType);
        }

        super.objectKey = objectKey;
        this.originalName = originalName;
        super.detailType = MediaFileExtension.valueOf(detailFileType);
    }

    @Builder
    public StaticMediaFile(String originalName, String extension, Long id, String objectKey, String bucketName,
                           String originalFileSize) {
        super(id, objectKey, FileType.STATIC_MEDIA_FILE, bucketName, MediaFileExtension.valueOf(extension), originalFileSize);
        this.originalName = originalName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
            return false;
        StaticMediaFile other = (StaticMediaFile) o;

        return Objects.equal(objectKey, other.getObjectKey())
                && Objects.equal(getObjectKey(), other.getObjectKey())
                && Objects.equal(detailType, other.getDetailType());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(objectKey, getObjectKey(), detailType);

    }
}
