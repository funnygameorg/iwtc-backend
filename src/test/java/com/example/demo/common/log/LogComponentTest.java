package com.example.demo.common.log;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;

import static org.assertj.core.api.Assertions.*;

public class LogComponentTest {

    LogComponent logComponent = new LogComponent();


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
}
