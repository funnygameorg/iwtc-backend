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

    @org.hibernate.annotations.Comment("클라이언트에서 미디어 노출을 위해 참조하기 위한 주소")
    protected String filePath;

    @org.hibernate.annotations.Comment("미디어 파일의 형태 (저장된 파일, 영상 링크...)")
    @Enumerated(value = EnumType.STRING)
    protected FileType fileType;
}
