package com.example.demo.domain.etc.model;

import com.google.common.base.Objects;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Comment;

import static com.example.demo.domain.etc.model.vo.FileType.STATIC_MEDIA_FILE;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@Table(name = "STATIC_MEDIA_FILE")
@NoArgsConstructor(access = PROTECTED)
public class StaticMediaFile extends MediaFile {

    @Comment("파일의 원래 이름")
    @NotNull
    @NotBlank
    private String originalName;

    @Comment("파일 확장자")
    @NotNull
    @NotBlank
    private String extension;



    public void update(String objectKey, String originalName) {

        super.objectKey = objectKey;
        this.originalName = originalName;

    }





    @Builder
    public StaticMediaFile(String originalName, String extension, Long id, String objectKey, String bucketName) {
        super(id, objectKey, STATIC_MEDIA_FILE, bucketName);
        this.originalName = originalName;
        this.extension = extension;
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
                && Objects.equal(extension, other.getExtension());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(objectKey, getObjectKey(), extension);

    }
}
