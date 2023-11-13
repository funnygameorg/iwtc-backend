package com.example.demo.domain.etc.model;

import com.google.common.base.Objects;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Comment;

import static com.example.demo.domain.etc.model.vo.FileType.STATIC_MEDIA_FILE;
import static lombok.AccessLevel.PRIVATE;
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

    @Comment("실제 저장하는데 사용한 파일 이름")
    @NotNull
    @NotBlank
    private String absoluteName;

    @Comment("파일 확장자")
    @NotNull
    @NotBlank
    private String extension;

    @Builder
    public StaticMediaFile(String originalName, String absoluteName, String extension, long id, String filePath) {
        super(id, filePath, STATIC_MEDIA_FILE);
        this.originalName = originalName;
        this.absoluteName = absoluteName;
        this.extension = extension;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
            return false;
        StaticMediaFile other = (StaticMediaFile) o;

        return Objects.equal(absoluteName, other.getAbsoluteName())
                && Objects.equal(getFilePath(), other.getFilePath())
                && Objects.equal(extension, other.getExtension());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(absoluteName, getFilePath(), extension);

    }
}