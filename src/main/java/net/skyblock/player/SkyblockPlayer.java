package net.skyblock.player;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.attribute.Attribute;
import net.minestom.server.network.packet.server.play.UpdateHealthPacket;
import net.minestom.server.network.player.GameProfile;
import net.minestom.server.network.player.PlayerConnection;
import net.minestom.server.timer.TaskSchedule;
import net.skyblock.command.base.RankableSender;
import net.skyblock.player.manager.PlayerAbilityManager;
import net.skyblock.player.manager.PlayerStatsManager;
import net.skyblock.player.rank.PlayerRank;
import net.skyblock.player.ui.SkyblockPlayerActionBar;
import net.skyblock.stats.definition.Statistic;
import org.jetbrains.annotations.NotNull;

/**
 * Extends vanilla Player with Skyblock-specific functionality including stats,
 * health/mana management, and rank privileges.
 */
public class SkyblockPlayer extends Player implements RankableSender {
    private PlayerStatsManager statsManager;
    private PlayerAbilityManager abilityManager;
    private PlayerRank playerRank = PlayerRank.DEFAULT;

    // Core player attributes
    private float currentHealth;
    private float currentMana;

    // UI Systems
    private SkyblockPlayerActionBar actionBar;

    public SkyblockPlayer(@NotNull PlayerConnection playerConnection, @NotNull GameProfile gameProfile) {
        super(playerConnection, gameProfile);
        getAttribute(Attribute.MAX_HEALTH).setBaseValue(40);
    }

    /**
     * Initializes recurring tasks for this player
     */
    public void initTaskLoop() {
        statsManager.recalculateAll();
        resetHealthAndMana();
        MinecraftServer.getSchedulerManager()
                .scheduleTask(this::taskLoop, TaskSchedule.immediate(), TaskSchedule.tick(2*20));
    }

    private void taskLoop() {
        if (!isDead()) {
            regenerateHealth();
            regenerateMana();
        }
        updateAttributes();
        actionBar.update();
    }

    /**
     * Resets player's health and mana to maximum values
     */
    public void resetHealthAndMana() {
        setHealth(getMaxHealth());
        setCurrentMana(getMaxMana());
    }

    /**
     * Regenerates player mana based on stats
     */
    public void regenerateMana() {
        double regenAmount = statsManager.calculateManaRegeneration();
        setCurrentMana((float) (getCurrentMana() + regenAmount));
    }

    /**
     * Regenerates player health based on stats
     */
    public void regenerateHealth() {
        double regenAmount = statsManager.calculateHealthRegeneration();
        setHealth((float) (getHealth() + regenAmount));
    }

    @Override
    public void setHealth(float healthToSet) {
        if (healthToSet <= 0) {
            kill();
            return;
        }
        float maxHealth = getMaxHealth();
        this.currentHealth = Math.min(healthToSet, maxHealth);
        this.sendPacket(new UpdateHealthPacket((currentHealth / maxHealth) * 40, 20, 20));
    }

    @Override
    public float getHealth() {
        return currentHealth;
    }

    @Override
    public void kill() {
        resetHealthAndMana();
    }

    /**
     * @return Maximum health from player stats
     */
    public float getMaxHealth() {
        return (float) statsManager.getStat(Statistic.HEALTH);
    }

    /**
     * @return Current mana value
     */
    public double getCurrentMana() {
        return currentMana;
    }

    /**
     * Sets player's current mana, if mana is larger than maxMana then its set to maxMana
     */
    public void setCurrentMana(float mana) {
        double maxMana = getMaxMana();
        this.currentMana = (float) Math.min(mana, maxMana);
    }

    /**
     * @return Maximum mana from player stats
     */
    public float getMaxMana() {
        return (float) statsManager.getStat(Statistic.INTELLIGENCE);
    }

    /**
     * Attempts to consume mana for ability usage
     *
     * @param amount Mana cost
     * @return true if successful, false if insufficient mana
     */
    public boolean consumeMana(double amount) {
        if (currentMana >= amount) {
            setCurrentMana((float) (currentMana - amount));
            return true;
        }
        return false;
    }

    /**
     * Sets the player's stats manager (can only be set once)
     */
    public void setStatsManager(@NotNull PlayerStatsManager statsManager) {
        if (this.statsManager != null) {
            throw new IllegalStateException("Stats manager has already been set");
        }
        this.statsManager = statsManager;
    }

    /**
     * Sets the player's action bar manager (can only be set once)
     */
    public void setActionBar(@NotNull SkyblockPlayerActionBar actionBar) {
        if (this.actionBar != null) {
            throw new IllegalStateException("ActionBar has already been set");
        }
        this.actionBar = actionBar;
    }

    /**
     * Sets the player's ability manager (can only be set once).
     *
     * @param abilityManager The ability manager to assign to this player
     * @throws IllegalStateException if an ability manager has already been set
     */
    public void setAbilityManager(@NotNull PlayerAbilityManager abilityManager) {
        if (this.abilityManager != null) {
            throw new IllegalStateException("AbilityManager has already been set");
        }
        this.abilityManager = abilityManager;
    }

    /**
     * Updates player attributes based on current stats
     * - Movement speed scaled down by 1000
     * - Entity interaction range based on swing range stat
     * - Attack speed calculated using bonus attack speed percentage
     */
    private void updateAttributes() {
        double speed = this.statsManager.getStat(Statistic.SPEED);
        double swingRange = this.statsManager.getStat(Statistic.SWING_RANGE);
        double attackSpeed = this.statsManager.getStat(Statistic.BONUS_ATTACK_SPEED);

        getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue(speed / 1000);
        getAttribute(Attribute.ENTITY_INTERACTION_RANGE).setBaseValue(swingRange);
        getAttribute(Attribute.ATTACK_SPEED).setBaseValue((10 / (1 + (attackSpeed / 100))));
    }

    public PlayerStatsManager getStatsManager() {
        return statsManager;
    }

    public PlayerAbilityManager getAbilityManager() {
        return abilityManager;
    }

    public void setPlayerRank(PlayerRank playerRank) {
        this.playerRank = playerRank;
    }

    public SkyblockPlayerActionBar getActionBar() {
        return actionBar;
    }

    @Override
    public @NotNull PlayerRank getRank() {
        return playerRank;
    }
}