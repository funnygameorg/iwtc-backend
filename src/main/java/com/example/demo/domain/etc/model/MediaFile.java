package com.example.demo.domain.etc.model;

import com.example.demo.common.jpa.TimeBaseEntity;
import com.example.demo.domain.etc.model.vo.FileType;
import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.InheritanceType.JOINED;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@Table(name = "MEDIA_FILE")
@AllArgsConstructor(access = PROTECTED)
@NoArgsConstructor(access = PROTECTED)
@Inheritance(strategy = JOINED)
@DiscriminatorColumn(name = "d_type")
public abstract class MediaFile extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    protected String filePath;

    @Enumerated(value = EnumType.STRING)
    protected FileType fileType;
}
