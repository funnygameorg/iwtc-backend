package com.masikga.error.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.Comment;

import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Entity
@Table(name = "ERROR_CODE_ENTITY",
        indexes = {@Index(name = "NAME__INDEX", columnList = "name")}
)
public class ErrorCodeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private long id;

    @NotNull
    @Comment("서버의 enum name과 동일한 값")
    private String name;

    @NotNull
    @Comment("에러코드 고유값")
    private int code;

    @NotNull
    @Comment("에러코드 메시지")
    private String message;

    @NotNull
    @Comment("사용자에게 반환할 때 사용되는 HttpStatus")
    private int httpStatus;
}
