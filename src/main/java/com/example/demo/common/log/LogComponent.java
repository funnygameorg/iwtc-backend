package com.example.demo.common.log;

import org.springframework.stereotype.Component;


/**
 * 로그에 적용하는 기능
 *
 */
@Component
public class LogComponent {

    // `data`의 값이 400자 이상이면 `...(reduce)`로 변경
    // 너무 길면 로그 확인에 불편함
    public String reduceLongString(String data) {
        if(data.length() >= 400) {
            return data.substring(0, 400) + "...(reduce)";
        }
        return data;
    }


}
