package com.mygdx.game.Objects;

public class Score {
    private int score;

    public Score() {
        score = 0;
    }

    public void increaseScore(int value) {
        score += value;
    }

    public int getScore() {
        return score;
    }

    public void reset() {
        score = 0;
    }
}
