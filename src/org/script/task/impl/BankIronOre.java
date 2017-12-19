package org.script.task.impl;

import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.script.MethodProvider;
import org.script.IronPmScript;
import org.script.task.MiningTask;
import org.script.util.SleepRange;

/**
 * A mining task that banks iron ore when the player's inventory is full.
 *
 * @author lare96 <http://github.org/lare96>
 */
public final class BankIronOre extends MiningTask {

    /**
     * Creates a new {@link BankIronOre}.
     */
    public BankIronOre(IronPmScript provider) {
        super(provider);
    }

    @Override
    public SleepRange onLoop() {
        if (provider().inventory.isFull()) {
            if (provider().bank.isOpen()) {
                provider().setStatus("Depositing iron ore into the bank.");
                provider().bank.depositAll();
            } else {
                provider().setStatus("Inventory full, walking to the bank.");
                getBank().interact("Use");
                return new SleepRange(1000, 3500);
            }
        }
        return SleepRange.NULL;
    }

    /**
     * Returns the closest appropriate bank or deposit box.
     */
    private RS2Object getBank() {
        return provider().objects.closest(MethodProvider.random(30) == 1 ?
                "Deposit box" : "Bank chest");
    }
}
