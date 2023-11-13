package com.example.demo.domain.worldcup.exception;

import com.example.demo.domain.worldcup.controller.request.ClearWorldCupGameRequest;
import com.example.demo.domain.worldcup.model.WorldCupGameContents;
import com.fasterxml.jackson.databind.introspect.DefaultAccessorNamingStrategy;

import java.util.List;

public record ClearWorldCupGameResponse(
        String firstWinnerName,
        String secondWinnerName,
        String thirdWinnerName,
        String fourthWinnerName
) {

        public static ClearWorldCupGameResponse build(
                List<WorldCupGameContents> contents
        ) {
                return new ClearWorldCupGameResponse(
                        contents.get(0).getName(),
                        contents.get(1).getName(),
                        contents.get(2).getName(),
                        contents.get(3).getName()
                );
        }
}