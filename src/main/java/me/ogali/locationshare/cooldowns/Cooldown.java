package me.ogali.locationshare.cooldowns;

import java.util.concurrent.TimeUnit;

public class Cooldown {

    private final long cooldownTimeInSeconds;

    private long cooldownStartTimeInSeconds = 0;

    public Cooldown(long cooldownTimeInSeconds) {
        this.cooldownTimeInSeconds = cooldownTimeInSeconds * 1000;
    }

    public void start() {
        cooldownStartTimeInSeconds = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() + cooldownTimeInSeconds);
    }

    public boolean isActive() {
        if (cooldownStartTimeInSeconds == 0) return false;
        return cooldownStartTimeInSeconds > TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
    }

    public long getRemainingTime() {
        final long timeLeft = cooldownStartTimeInSeconds - TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
        return Math.max(timeLeft, 0);
    }

}
