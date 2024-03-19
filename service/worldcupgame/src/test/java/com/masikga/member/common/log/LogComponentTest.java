package com.masikga.member.common.log;

import com.masikga.worldcupgame.common.log.LogComponent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LogComponentTest {

	com.masikga.worldcupgame.common.log.LogComponent logComponent = new LogComponent();

	@Test
	@DisplayName("연속되는 숫자나 숫자 1개는 '*'로 변환한다.")
	public void replaceNumberToStarTest() {

		var data = "a17A[aa99]9+9";
		var result = logComponent.replaceNumberToStar(data);

		assertThat(result).isEqualTo("a*A[aa*]*+*");
	}

	@Test
	@DisplayName("숫자가 존재하지 않는 값은 입력과 같은 값이 나온다.")
	public void replaceNumberToStarTest2() {

		var data = "/Core/Skin/Login.aspx";
		var result = logComponent.replaceNumberToStar(data);

		assertThat(result).isEqualTo("/Core/Skin/Login.aspx");
	}

	@Test
	@DisplayName("미디어 파일 조회와 관련된 경우 True를 반환")
	public void excludeMediaFileRequestTest() {

		var result = logComponent.excludeMediaFileRequest("/api/media-files/1");

		assertThat(result).isTrue();
	}
}
