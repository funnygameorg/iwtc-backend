package com.example.demo.domain.etc.model;

import com.example.demo.common.jpa.TimeBaseEntity;
import com.example.demo.domain.member.model.Member;
import com.google.common.base.Objects;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@Builder
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
public class MediaFile extends TimeBaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    private String originalName;

    @NotNull
    @NotBlank
    private String absoluteName;

    @NotNull
    @NotBlank
    private String filePath;

    @NotNull
    @NotBlank
    private String extension;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
            return false;
        MediaFile other = (MediaFile) o;

        return Objects.equal(absoluteName, other.absoluteName)
                && Objects.equal(filePath, other.filePath)
                && Objects.equal(extension, other.extension);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(absoluteName, filePath, extension);

    }
}
