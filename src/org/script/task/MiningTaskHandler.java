package org.script.task;

import org.script.util.SleepInterval;

import java.util.ArrayList;
import java.util.List;

/**
 * A handler that runs the bot by executing mining tasks.
 *
 * @author lare96 <http://github.org/lare96>
 */
public final class MiningTaskHandler {

    /**
     * A constant representing the default sleep interval.
     */
    private static final SleepInterval DEFAULT = new SleepInterval(500, 1500);

    /**
     * A list of mining tasks.
     */
    private final List<MiningTask> miningTasks = new ArrayList<>();

    /**
     * Adds a new mining task.
     */
    public void add(MiningTask mt) {
        miningTasks.add(mt);
    }

    /**
     * Exclusively and directly called from the main script's event loop. Runs the bot by executing all
     * mining tasks in their proper order.
     */
    public SleepInterval onLoop() {
        for(MiningTask mt : miningTasks) {
            SleepInterval sleep = mt.onLoop();
            if(sleep != SleepInterval.NULL) {
                return sleep;
            }
        }
        return DEFAULT;
    }
}
