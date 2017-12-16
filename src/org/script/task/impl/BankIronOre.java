package org.script.task.impl;

import org.osbot.rs07.api.model.RS2Object;
import org.script.IronPmScript;
import org.script.task.MiningTask;
import org.script.util.SleepInterval;

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
    public SleepInterval onLoop() {
        if(provider().inventory.isFull()) {
            if (provider().bank.isOpen()){
                provider().setStatus("Depositing iron ore into the bank.");
                provider().bank.depositAll();
            } else {
                provider().setStatus("Inventory full, walking to the bank.");

                RS2Object bank = provider().objects.closest("Bank chest");
                bank.interact("Use");

                return new SleepInterval(1000, 3500);
            }
        }
        return SleepInterval.NULL;
    }
}
