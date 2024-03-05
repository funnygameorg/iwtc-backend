package com.masikga.itwc.common.error.exception;

import com.masikga.itwc.common.error.CustomErrorCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@Getter
@NoArgsConstructor(access = PRIVATE)
@ToString
public abstract class BaseException extends RuntimeException {

	protected BaseException(String publicMessage, CustomErrorCode errorCode, HttpStatus httpStatus) {
		this.publicMessage = publicMessage;
		this.errorCode = errorCode;
		this.errorTime = LocalDateTime.now();
		this.httpStatus = httpStatus;
		this.errorId = new StringBuilder()
			.append(errorTime)
			.append("__")
			.append(UUID.randomUUID())
			.toString();
	}

	LocalDateTime errorTime;
	String errorId;
	String publicMessage;

	String privateMessage; // TODO : 사용자에게 노출되는 내용, 노출되면 안되는 내용 분리하기
	CustomErrorCode errorCode;
	HttpStatus httpStatus;
}
