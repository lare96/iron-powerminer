package org.script.util;

import org.osbot.rs07.script.MethodProvider;

public final class SleepInterval {

    public static final SleepInterval NULL = new SleepInterval(-1, -1);

    private final int lower;
    private final int upper;

    public SleepInterval(int lower, int upper) {
        this.lower = lower;
        this.upper = upper;
    }

    public int sleepAt() {
        return MethodProvider.random(lower, upper);
    }

    public int getLower() {
        return lower;
    }

    public int getUpper() {
        return upper;
    }
}
