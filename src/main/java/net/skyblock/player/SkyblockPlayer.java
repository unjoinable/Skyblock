package net.skyblock.player;

import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.TaskSchedule;
import net.skyblock.item.SkyblockItem;
import net.skyblock.player.rank.PlayerRank;
import net.skyblock.stats.StatHolder;
import net.skyblock.stats.StatProfile;
import net.skyblock.stats.Statistic;
import net.skyblock.stats.combat.CombatEntity;
import net.skyblock.stats.combat.DamageType;
import net.skyblock.stats.combat.SkyblockDamage;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;
import net.minestom.server.network.player.GameProfile;
import net.minestom.server.network.player.PlayerConnection;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a player in the Skyblock game, extending {@link Player} to include additional
 * stats, combat functionality, and mana management.
 * <p>
 * This class implements the {@link CombatEntity} interface to handle combat interactions
 * and integrates with the stat system to calculate health, mana, and other combat attributes.
 */
public class SkyblockPlayer extends Player implements StatHolder, CombatEntity {
    private final PlayerStatsManager statsManager;
    private PlayerRank playerRank;

    // Combat-related fields
    private double currentHealth;
    private double currentMana;
    private boolean invulnerable;

    // Systems
    private final SkyblockPlayerActionBar actionBar;

    /**
     * Constructs a {@link SkyblockPlayer} instance, initializing the player's connection,
     * game profile, and stats manager.
     * <p>
     * The player is assigned a default rank of {@link PlayerRank#DEFAULT}.
     *
     * @param playerConnection the connection of the player
     * @param gameProfile the game profile of the player
     */
    public SkyblockPlayer(@NotNull PlayerConnection playerConnection, @NotNull GameProfile gameProfile) {
        super(playerConnection, gameProfile);
        this.statsManager = new PlayerStatsManager(this);
        this.playerRank = PlayerRank.DEFAULT;

        // Initialize health and mana to maximum values
        resetHealthAndMana();

        // Systems
        this.actionBar = new SkyblockPlayerActionBar(this);
    }

    public void initTaskLoop() {
        MinecraftServer.getSchedulerManager().scheduleTask(() -> {
            taskLoop();
        }, TaskSchedule.tick(4), TaskSchedule.tick(2*20));
    }

    private void taskLoop() {
        regenerateHealth();
        regenerateMana();

        actionBar.update();
    }

    /**
     * Gets the stat profile associated with this player.
     * <p>
     * This method delegates the request to the player's {@link PlayerStatsManager} to retrieve the
     * player's statistics.
     *
     * @return the {@link StatProfile} of the player
     */
    @Override
    public @NotNull StatProfile getStatProfile() {
        return this.statsManager.getStatProfile();
    }

    /**
     * Sets the rank of this player.
     * <p>
     * This method allows for changing the player's rank to a new {@link PlayerRank}.
     *
     * @param playerRank the new rank to assign to this player
     */
    public void setPlayerRank(PlayerRank playerRank) {
        this.playerRank = playerRank;
    }

    /**
     * Gets the current rank of the player.
     *
     * @return the player's current {@link PlayerRank}
     */
    public PlayerRank getPlayerRank() {
        return playerRank;
    }

    /**
     * Gets the stats manager for this player.
     * <p>
     * The stats manager handles the player's stat profile and provides methods for updating
     * or retrieving individual stats.
     *
     * @return the {@link PlayerStatsManager} for the player
     */
    public PlayerStatsManager getStatsManager() {
        return statsManager;
    }

    /**
     * Resets the player's health and mana to their maximum values.
     * Useful when respawning or initializing the player.
     */
    public void resetHealthAndMana() {
        this.currentHealth = getMaxHealth();
        this.currentMana = getMaxMana();
    }

    /**
     * Regenerates player health based on stats.
     * This should be called from a scheduled task.
     */
    public void regenerateHealth() {
        if (isDead()) return;

        double regenAmount = statsManager.calculateHealthRegeneration();
        setCurrentHealth(getCurrentHealth() + regenAmount);
    }

    /**
     * Regenerates player mana based on stats.
     * This should be called from a scheduled task.
     */
    public void regenerateMana() {
        if (isDead()) return;

        double regenAmount = statsManager.calculateManaRegeneration();
        setCurrentMana(getCurrentMana() + regenAmount);
    }

    /* === CombatEntity Interface Implementation === */

    @Override
    public Entity getEntity() {
        return this;
    }

    @Override
    public double getCurrentHealth() {
        return currentHealth;
    }

    @Override
    public void setCurrentHealth(double health) {
        double maxHealth = getMaxHealth();
        this.currentHealth = Math.max(0.0, Math.min(health, maxHealth));

        //TODO: Update client-side health display
        double healthPercentage = this.currentHealth / maxHealth;
        this.setHealth((float) (healthPercentage * 20.0)); // 40 hearts

        // If health drops to 0, trigger death
        if (this.currentHealth <= 0.0) {
            kill();
        }
    }

    @Override
    public double getMaxHealth() {
        return getStatProfile().get(Statistic.HEALTH);
    }

    /**
     * Gets the current mana of the player
     *
     * @return current mana value
     */
    public double getCurrentMana() {
        return currentMana;
    }

    /**
     * Sets the current mana of the player
     *
     * @param mana the new mana value
     */
    public void setCurrentMana(double mana) {
        double maxMana = getMaxMana();
        this.currentMana = Math.max(0.0, Math.min(mana, maxMana));
    }

    /**
     * Gets the maximum possible mana of the player
     *
     * @return maximum mana value
     */
    public double getMaxMana() {
        return getStatProfile().get(Statistic.INTELLIGENCE);
    }

    /**
     * Consumes mana for ability usage
     *
     * @param amount the amount of mana to consume
     * @return true if enough mana was available and consumed; false otherwise
     */
    public boolean consumeMana(double amount) {
        if (currentMana >= amount) {
            setCurrentMana(currentMana - amount);
            return true;
        }
        return false;
    }

    @Override
    public boolean isInvulnerable() {
        return invulnerable;
    }

    @Override
    public void setInvulnerable(boolean invulnerable) {
        this.invulnerable = invulnerable;
    }

    @Override
    public void attack(CombatEntity target, DamageType damageType) {
        if (target == null || target.isInvulnerable()) {
            return;
        }

        SkyblockItem weapon = ItemSlot.MAIN_HAND.getItem(this);
        SkyblockDamage damage = DamageCalculator.calculateDamage(this, target, weapon, damageType);

        target.damage(damage);
    }

    @Override
    public void damage(SkyblockDamage damage) {
        if (isInvulnerable() || isDead()) {
            return;
        }

        double finalDamage = damage.getRawDamage();
        StatProfile stats = getStatProfile();

        // Reduction by Defense
        if (!damage.getDamageType().bypassesDefense()) {
            double defense = stats.get(Statistic.DEFENSE);
            finalDamage = finalDamage * (1.0 - (defense / (defense + 100.0)));
        }

        // Reduction by True Defense
        if (damage.getDamageType().bypassesDefense()) {
            double trueDefense = stats.get(Statistic.TRUE_DEFENSE);
            finalDamage = finalDamage * (1.0 - (trueDefense / (trueDefense + 100.0)));
        }

        // Apply the damage
        setCurrentHealth(getCurrentHealth() - finalDamage);

        // Display damage indicator
        spawnDamageIndicator(damage);

        // Apply knockback if source exists
        if (damage.getSourceEntity() != null) {
            applyKnockback(damage.getSourceEntity().getEntity());
        }
    }

    @Override
    public void applyKnockback(Entity source) { //TODO fix the knockback
        if (source == null) return;

        // Calculate knockback direction
        Pos sourcePos = source.getPosition();
        Pos myPos = getPosition();

        // Get direction vector from source to this entity
        double dx = myPos.x() - sourcePos.x();
        double dz = myPos.z() - sourcePos.z();

        // Normalize and apply knockback strength
        double length = Math.sqrt(dx * dx + dz * dz);
        if (length > 0) {
            double knockbackStrength = 0.4; // Base knockback strength
            dx = dx / length * knockbackStrength;
            dz = dz / length * knockbackStrength;

            // Apply velocity
            setVelocity(getVelocity().add(dx, 0.4, dz)); // 0.4 is upward knockback
        }
    }

    @Override
    public void spawnDamageIndicator(SkyblockDamage damage) {}

    @Override
    public void kill() {
        this.currentHealth = 0.0;
    }

    /**
     * Gets the player's action bar manager.
     *
     * @return The action bar manager for this player
     */
    public SkyblockPlayerActionBar getActionBar() {
        return actionBar;
    }

    /**
     * Convenience method to perform a melee attack on a target
     *
     * @param target the entity to attack
     */
    public void meleeDamage(CombatEntity target) {
        attack(target, DamageType.MELEE);
    }
}