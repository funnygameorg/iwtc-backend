package com.masikga.itwc.common.error.entity;

import static jakarta.persistence.GenerationType.*;

import org.hibernate.annotations.Comment;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

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
