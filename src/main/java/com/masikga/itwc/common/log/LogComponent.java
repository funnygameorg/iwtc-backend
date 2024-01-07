package com.masikga.itwc.common.log;

import java.util.List;

/**
 * 로그에 적용하는 기능
 *
 */
public class LogComponent {

	private List<String> logExcludeList = List.of("/api/media-files/", "getMediaFiles",
		"getMediaFile");

	// `data`의 값이 400자 이상이면 `...(reduce)`로 변경
	// 너무 길면 로그 확인에 불편함
	public String reduceLongString(String data) {
		if (data.length() >= 400) {
			return data.substring(0, 400) + "...(reduce)";
		}
		return data;
	}

	// 모든 숫자를 '*'로 변경
	public String replaceNumberToStar(String data) {
		return data.replaceAll("\\d+", "*");
	}

	// 미디어 파일 조회와 관련된 경우 `True`를 반환
	public boolean excludeMediaFileRequest(String target) {
		return logExcludeList.contains(target) || target.contains(logExcludeList.get(0));
	}
}
