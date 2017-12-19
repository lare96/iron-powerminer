package org.script.task.impl;

import org.osbot.rs07.api.ui.Skill;
import org.script.IronPmScript;
import org.script.task.MiningTask;
import org.script.util.SleepRange;

/**
 * A mining task that activates the {@code Dragon Pickaxe} special attack.
 *
 * @author lare96 <http://github.org/lare96>
 */
public final class ActivateSpecialAttack extends MiningTask {

    /**
     * A constant representing the pickaxes that have special attacks.
     */
    private static final String[] PICKAXES = {"Dragon pickaxe", "Infernal pickaxe", "Dragon pickaxe (or)"};

    /**
     * Creates a new {@link ActivateSpecialAttack}.
     */
    public ActivateSpecialAttack(IronPmScript provider) {
        super(provider);
    }

    @Override
    public SleepRange onLoop() {
        boolean activateSpecial = provider().equipment.contains(PICKAXES);
        int dynamicLvl = provider().skills.getDynamic(Skill.MINING);
        int staticLvl = provider().skills.getStatic(Skill.MINING);

        if (dynamicLvl == staticLvl &&
                provider().combat.getSpecialPercentage() == 100 &&
                activateSpecial) {
            provider().setStatus("Activating the special attack boost.");
            provider().combat.toggleSpecialAttack(true);
        }
        return SleepRange.NULL;
    }
}
