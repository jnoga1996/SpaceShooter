package com.mygdx.game.Objects;

import com.badlogic.gdx.utils.TimeUtils;

public class GameClock {
    private static final long TIME_LIMIT = 900;
    private long startTime;

    public GameClock() {
        this.startTime = TimeUtils.millis();
    }

    public boolean levelCleared() {
        return getElpasedTime() > TIME_LIMIT;
    }

    public long getElpasedTime() {
        return TimeUtils.millis() - startTime;
    }

    public int getElpasedTimePercentage() {
        return (int)(getElpasedTime() / TIME_LIMIT);
    }
}
