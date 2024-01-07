package com.masikga.itwc.common.error;

import static lombok.AccessLevel.*;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
