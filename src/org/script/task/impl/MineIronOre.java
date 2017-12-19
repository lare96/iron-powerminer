package org.script.task.impl;

import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.model.RS2Object;
import org.script.IronPmScript;
import org.script.task.MiningTask;
import org.script.util.SleepRange;

import java.util.Optional;

/**
 * A mining task that powermines iron ore.
 *
 * @author lare96 <http://github.org/lare96>
 */
public final class MineIronOre extends MiningTask {

    /**
     * The ore currently being mined.
     */
    private RS2Object currentOre;

    /**
     * The next ore that will be mined.
     */
    private RS2Object nextOre;

    /**
     * Creates a new {@link MineIronOre}.
     */
    public MineIronOre(IronPmScript provider) {
        super(provider);
    }

    @Override
    public SleepRange onLoop() {
        if (!provider().inventory.isFull() && provider().inCorrectSpot()) {
            if (!provider().isSpotFree()) {
                provider().resetMiningPos();
                provider().walkToSpot();
                return new SleepRange(1500, 4000);
            } else {
                powermine();
                return new SleepRange(500, 2000);
            }
        }
        return SleepRange.NULL;
    }

    /**
     * Allows the bot to powermine iron ore.
     */
    private void powermine() {
        Runnable initialMine = () -> {
            provider().setStatus("Mining iron ore.");
            retrieveOre().ifPresent(this::mine);
        };

        if (currentOre == null) {
            initialMine.run();
            return;
        }

        if (currentOre.exists() && nextOre == null) {
            provider().setStatus("Hovering over next ore.");
            retrieveOre().ifPresent(this::hover);
        } else if (!currentOre.exists() && nextOre.exists()) {
            provider().setStatus("Mining hovered ore.");
            mine(nextOre);
            nextOre = null;
        } else {
            currentOre = null;
            nextOre = null;
            initialMine.run();
        }
    }

    /**
     * Interacts with the ore.
     */
    private void mine(RS2Object ore) {
        if (ore.interact("Mine")) {
            currentOre = ore;
        }
    }

    /**
     * Hovers over the ore.
     */
    private void hover(RS2Object ore) {
        if (ore.hover()) {
            nextOre = ore;
        }
    }

    /**
     * Retrieves the closest available ore.
     */
    private Optional<RS2Object> retrieveOre() {
        Area miningArea = provider().myPlayer().getArea(1);

        return provider().objects.getAll().stream().
                filter(RS2Object::exists).
                filter(miningArea::contains).
                filter(obj -> !obj.equals(currentOre)).
                findFirst();
    }
}
