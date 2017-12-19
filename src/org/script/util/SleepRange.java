package org.script.util;

import org.osbot.rs07.script.MethodProvider;

/**
 * A model representing a range of time to sleep for.
 *
 * @author lare96 <http://github.org/lare96>
 */
public final class SleepRange {

    /**
     * A nil sleep range implementation. This is used to 'skip' events in the mining task
     * pipeline without sleeping.
     */
    public static final SleepRange NULL = new SleepRange(-1, -1);

    /**
     * The lower value.
     */
    private final int lower;

    /**
     * The upper value.
     */
    private final int upper;

    /**
     * Creates a new {@link SleepRange}.
     */
    public SleepRange(int lower, int upper) {
        this.lower = lower;
        this.upper = upper;
    }

    /**
     * Returns a random value between the lower and upper bounds.
     */
    public int sleepAt() {
        return MethodProvider.random(lower, upper);
    }

    /**
     * @return The lower value.
     */
    public int getLower() {
        return lower;
    }

    /**
     * @return The upper value.
     */
    public int getUpper() {
        return upper;
    }
}
