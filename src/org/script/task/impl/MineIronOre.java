package org.script.task.impl;

import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.model.RS2Object;
import org.script.IronPmScript;
import org.script.task.MiningTask;
import org.script.util.SleepInterval;

import java.util.Optional;

/**
 * A mining task that powermines iron ore.
 *
 * @author lare96 <http://github.org/lare96>
 */
public final class MineIronOre extends MiningTask {

    // TODO: Mine ore, hover next ore, then click as soon as ore is depleted

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
    public SleepInterval onLoop() {
        if (!provider().inventory.isFull() && provider().inCorrectSpot()) {
            if (!provider().isSpotFree()) {
                provider().resetMiningPos();
                provider().walkToSpot();
                return new SleepInterval(1500, 4000);
            } else {
                mineIronOre();
                return new SleepInterval(700, 2000);
            }
        }
        return SleepInterval.NULL;
    }

    /**
     * Allows the bot to mine iron ore.
     */
    private void mineIronOre() {
        provider().setStatus("Powermining iron ore...");

        if (currentOre == null) {
            RS2Object mineOre = retrieveOre();
            if (mineOre == null) {
                return;
            }
            if (mineOre.interact("Mine")) {
                currentOre = mineOre;
                nextOre = null;
            }
        } else if (currentOre.exists() && nextOre == null) {
            RS2Object hoverOre = retrieveOre();
            if (hoverOre == null) {
                return;
            }
            hoverOre.hover();
            nextOre = hoverOre;
        } else if (!currentOre.exists() && nextOre != null && nextOre.exists()) {
            if (nextOre.interact("Mine")) {
                currentOre = nextOre;
                nextOre = null;
            }
        }
    }

    private RS2Object retrieveOre() {
        Area miningArea = provider().myPlayer().getArea(1);

        return provider().objects.getAll().stream().
                filter(RS2Object::exists).
                filter(miningArea::contains).
                findFirst().orElse(null);
    }
}
