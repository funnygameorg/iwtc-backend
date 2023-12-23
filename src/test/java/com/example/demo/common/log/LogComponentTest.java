package com.example.demo.common.log;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class LogComponentTest {

    LogComponent logComponent = new LogComponent();


    @Test
    @DisplayName("0 - 9의 값은 '*'로 변환한다.")
    public void replaceNumberToStarTest() {

        var data = "a17A[aa99]9+9";
        var result = logComponent.replaceNumberToStar(data);

        Assertions.assertThat(result).isEqualTo("a**A[aa**]*+*");
    }

}
