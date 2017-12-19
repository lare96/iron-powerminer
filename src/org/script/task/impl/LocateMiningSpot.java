package org.script.task.impl;

import org.script.IronPmScript;
import org.script.task.MiningTask;
import org.script.util.SleepRange;

/**
 * A mining task that guides the player to the right mining spot.
 *
 * @author lare96 <http://github.org/lare96>
 */
public final class LocateMiningSpot extends MiningTask {

    /**
     * Creates a new {@link LocateMiningSpot}.
     */
    public LocateMiningSpot(IronPmScript provider) {
        super(provider);
    }

    @Override
    public SleepRange onLoop() {
        if (!provider().inventory.isFull() &&
                !provider().inCorrectSpot() &&
                !provider().myPlayer().isMoving()) {
            provider().setStatus(getMessage());
            provider().walkToSpot();
            return new SleepRange(1000, 3500);
        }
        return SleepRange.NULL;
    }

    /**
     * Returns the correct script status message.
     */
    private String getMessage() {
        return provider().getMiningPos() == null ?
                "Looking for mining spot." : "Returning to mining spot.";
    }
}