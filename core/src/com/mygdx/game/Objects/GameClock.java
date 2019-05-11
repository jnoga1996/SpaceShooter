package com.mygdx.game.Objects;

import com.badlogic.gdx.utils.Timer;

public class GameClock {
    private static final double TIME_LIMIT = 30.0;
    private Timer timer;
    private long elapsedTime = 0;

    public GameClock() {
        timer = new Timer();
        timer.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                incrementElapsedTime();
            }
        }, 0, 1);
        timer.start();
    }

    private void incrementElapsedTime() {
        elapsedTime++;
    }

    public boolean levelCleared() {
        return getElapsedTime() > TIME_LIMIT;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    public double getElapsedTimePercentage() {
        return (getElapsedTime() / TIME_LIMIT) * 100;
    }
}
