package com.example.demo.domain.worldcup.utils;

import org.springframework.stereotype.Component;

@Component
public class WorldCupGameScoreUtils {

    private static final int FIRST_SCORE = 10;
    private static final int SECOND_SCORE = 7;
    private static final int THIRD_SCORE = 3;
    private static final int FOURTH_SCORE = 3;

    private static final String FIRST_WINNER_KEY = "first";
    private static final String SECOND_WINNER_KEY = "second";
    private static final String THIRD_WINNER_KEY = "third";
    private static final String FOURTH_WINNER_KEY = "fourth";


    public int scoreGenerate(String winnerRank) {
        return switch (winnerRank) {
            case FIRST_WINNER_KEY -> FIRST_SCORE;
            case SECOND_WINNER_KEY -> SECOND_SCORE;
            case THIRD_WINNER_KEY -> THIRD_SCORE;
            case FOURTH_WINNER_KEY -> FOURTH_SCORE;
            default -> 0;
        };
    }
}
