package org.script.task;

import org.script.IronPmScript;
import org.script.util.SleepRange;

/**
 * A model that provides implementation for mining tasks.
 *
 * @author lare96 <http://github.org/lare96>
 */
public abstract class MiningTask {

    /**
     * The main script provider.
     */
    private final IronPmScript provider;

    /**
     * Creates a new {@link MiningTask}.
     */
    public MiningTask(IronPmScript provider) {
        this.provider = provider;
    }

    /**
     * Ran when its this task's turn to execute.
     */
    public abstract SleepRange onLoop();

    /**
     * Returns the main script provider.
     */
    protected IronPmScript provider() {
        return provider;
    }
}
