package com.masikga.itwc.common.error;

import lombok.*;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PRIVATE;

@Getter
@ToString
@Builder
@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
public class CustomErrorResponse {

	private LocalDateTime errorTime;
	private int errorCode;
	private String message;
	private String errorId;
}
