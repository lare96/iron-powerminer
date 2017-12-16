package org.script;

import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.ui.Message;
import org.osbot.rs07.api.ui.Message.MessageType;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.event.WalkingEvent;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;
import org.script.task.impl.ActivateSpecialAttack;
import org.script.task.impl.BankIronOre;
import org.script.task.impl.LocateMiningSpot;
import org.script.task.impl.MineIronOre;
import org.script.task.MiningTaskHandler;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.text.NumberFormat;
import java.time.LocalTime;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * The main class of this script.
 *
 * @author lare96 <http://github.org/lare96>
 */
@ScriptManifest(logo = "", name = "[IronPmScript] v2.2", author = "lare96", version = 2.2, info = "Will powermine iron in the updated members guild.")
public final class IronPmScript extends Script {

    // TODO: add optional dropping functionality
    // TODO: add usage of bank chest or deposit box functionality
    // TODO: optional idle system, to reduce ban rate
    // TODO: stick-with-first-rock and hop, or run to second rock and hop functionality
    // TODO: stick with one spot, the entire time (chosen on the first iteration)

    /**
     * A constant representing the primary mining spot.
     */
    private static final Position SPOT_1 = new Position(3021, 9721, 0);

    /**
     * A constant representing the secondary mining spot.
     */
    private static final Position SPOT_2 = new Position(3029, 9720, 0);

    /**
     * A constant representing both primary and secondary mining spots.
     */
    private static final Position[] SPOTS = {SPOT_1, SPOT_2};

    /**
     * A constant representing the message received when ore is mined.
     */
    private static final String ORE_MESSAGE = "You manage to mine some iron.";

    /**
     * A timestamp, made when this script is started.
     */
    private final long timeMod = System.currentTimeMillis();

    /**
     * A function that retrieves how long this script has been running, in seconds.
     */
    private final Supplier<Long> timeRunning =
            () -> TimeUnit.SECONDS.convert(System.currentTimeMillis() - timeMod, TimeUnit.MILLISECONDS);

    /**
     * The script's current status.
     */
    private String status = "Idle...";

    /**
     * The player's current mining spot.
     */
    private Position miningPos;

    /**
     * The amount of ores the player has mined.
     */
    private int oresMined;

    /**
     * A mining task handler.
     */
    private final MiningTaskHandler handler = new MiningTaskHandler();

    /**
     * The experience number formatter.
     */
    private final NumberFormat formatXp = NumberFormat.getIntegerInstance();

    @Override
    public void onStart() throws InterruptedException {
        experienceTracker.start(Skill.MINING);

        handler.add(new LocateMiningSpot(this));
        handler.add(new MineIronOre(this));
        handler.add(new BankIronOre(this));
        handler.add(new ActivateSpecialAttack(this));
    }

    @Override
    public int onLoop() throws InterruptedException {
        return handler.onLoop().sleepAt();
    }

    @Override
    public void onMessage(Message msg) {
        if (msg.getType() == MessageType.GAME &&
                msg.getMessage().equals(ORE_MESSAGE)) {
            oresMined++;
        }
    }

    @Override
    public void onPaint(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Myriad Pro", Font.BOLD, 16));
        g.drawString("[IronPowerminer] v2.2", 7, 225);
        g.setFont(new Font("Myriad Pro", Font.PLAIN, 14));

        LocalTime timePassed = LocalTime.ofSecondOfDay(timeRunning.get());
        g.drawString("Time Running: " + timePassed, 7, 245);

        g.drawString("Status: " + status, 7, 260);

        long gainedXp = experienceTracker.getGainedXP(Skill.MINING);
        long gainedXpPerHour = experienceTracker.getGainedXPPerHour(Skill.MINING);
        g.drawString("XP: +" + formatXp.format(gainedXp) + " (" + formatXp.format(gainedXpPerHour) + ")", 7, 275);

        int gainedLvls = experienceTracker.getGainedLevels(Skill.MINING);
        g.drawString("Level: +" + gainedLvls, 7, 290);

        long secondsUntilLvl = TimeUnit.SECONDS.convert(experienceTracker.getTimeToLevel(Skill.MINING),
                TimeUnit.MILLISECONDS);
        LocalTime timeUntilLvl = LocalTime.ofSecondOfDay(secondsUntilLvl);
        g.drawString("Next Level In: " + timeUntilLvl, 7, 305);

        g.drawString("Ores Mined: " + formatXp.format(oresMined), 7, 320);
    }

    /**
     * Returns the amount of players on {@code pos}.
     */
    private int playerCount(Position pos) {
        return players.get(pos.getX(), pos.getY()).size();
    }

    /**
     * Returns {@code true} if the player is the only one on their current spot.
     */
    public boolean isSpotFree() {
       return players.get(myPosition().getX(), myPosition().getY()).stream().allMatch(myPlayer()::equals);
    }

    /**
     * Returns {@code true} if the player is in the correct spot.
     */
    public boolean inCorrectSpot() {
        Position pos = myPosition();
        return pos.equals(SPOT_1) || pos.equals(SPOT_2);
    }

    /**
     * Walks the player to one of the two mining spots. Hops worlds if both spots are taken.
     */
    public void walkToSpot() {
        if (miningPos != null) {
            WalkingEvent event = new WalkingEvent(miningPos);
            event.setMinDistanceThreshold(0);
            event.setOperateCamera(true);

            execute(event);
        } else {
            for (Position pos : SPOTS) {
                if (playerCount(pos) == 0) {
                    walking.walk(pos);
                    miningPos = pos;
                    return;
                }
            }
            worlds.hopToP2PWorld();
        }
    }

    /**
     * Resets the current mining position.
     */
    public void resetMiningPos() {
        miningPos = null;
    }

    /**
     * Sets the script status.
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Returns the current mining position.
     */
    public Position getMiningPos() {
        return miningPos;
    }
}
