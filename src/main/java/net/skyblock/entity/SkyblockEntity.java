package net.skyblock.entity;

import net.minestom.server.entity.attribute.Attribute;
import net.skyblock.stats.StatProfile;
import net.skyblock.stats.Statistic;
import net.skyblock.stats.combat.CombatEntity;
import net.skyblock.stats.combat.DamageType;
import net.skyblock.stats.combat.SkyblockDamage;
import net.skyblock.utils.MiniString;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.minestom.server.component.DataComponents;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.ai.GoalSelector;
import net.minestom.server.entity.ai.TargetSelector;
import net.minestom.server.instance.Instance;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Abstract base class for all Skyblock entities.
 * Implements the CombatEntity interface to provide standard combat functionality.
 */
public abstract class SkyblockEntity extends EntityCreature implements CombatEntity {
    private static final List<SkyblockEntity> activeMobs = new ArrayList<>();
    private int level = 0;
    private StatProfile statProfile;
    private double currentHealth;
    private boolean isInvulnerable = false;

    /**
     * Creates a new SkyblockEntity with the specified entity type
     *
     * @param entityType the Minestom entity type
     */
    protected SkyblockEntity(@NotNull EntityType entityType) {
        super(entityType);
        this.statProfile = new StatProfile(false); // Initialize empty profile
        this.currentHealth = 0; // Will be set during spawn based on max health
    }

    /**
     * Gets the name of this entity
     *
     * @return the entity name
     */
    public abstract @NotNull String name();

    /**
     * Gets the AI goal selectors for this entity at the specified level
     *
     * @param lvl the entity level
     * @return list of goal selectors
     */
    public abstract List<GoalSelector> getGoalSelectors(int lvl);

    /**
     * Gets the AI target selectors for this entity at the specified level
     *
     * @param lvl the entity level
     * @return list of target selectors
     */
    public abstract List<TargetSelector> getTargetSelectors(int lvl);

    /**
     * Configure the stats of this entity based on its level
     *
     * @param lvl the entity level
     */
    protected abstract void configureStats(int lvl);

    /**
     * Spawns this entity in the specified instance at the specified position with the given level
     *
     * @param lvl the entity level
     * @param instance the instance to spawn in
     * @param spawnPos the position to spawn at
     */
    public void spawn(int lvl, Instance instance, Pos spawnPos) {
        setCustomNameVisible(true);
        level = lvl;

        // Configure stats based on level
        configureStats(lvl);

        // Set initial health to max health
        this.currentHealth = getMaxHealth();

        // Set movement speed based on stats
        double speedStat = getStatProfile().get(Statistic.SPEED);
        getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue((speedStat / 1000) * 2.5);

        // Set custom name and visibility settings
        set(DataComponents.CUSTOM_NAME, displayName());
        setAutoViewable(true);
        setAutoViewEntities(true);

        // Set AI behavior
        addAIGroup(getGoalSelectors(lvl), getTargetSelectors(lvl));

        // Add to active mobs and spawn
        activeMobs.add(this);
        setInstance(instance, spawnPos);
    }

    /**
     * Generates the display name component for this entity
     *
     * @return the formatted display name
     */
    public Component displayName() {
        return MiniString.component(
                "<dark_gray>[<gray>Lv<lvl></gray>] <red><name> <green><cur_health:'#'>/<total_health:'#'></green>❤",
                Placeholder.parsed("lvl", String.valueOf(level)),
                Placeholder.parsed("name", name()),
                Formatter.number("cur_health", getCurrentHealth()),
                Formatter.number("total_health", getMaxHealth())
        );
    }

    @Override
    public @NotNull Entity getEntity() {
        return this;
    }

    @Override
    public StatProfile getStatProfile() {
        return statProfile;
    }

    @Override
    public double getCurrentHealth() {
        return currentHealth;
    }

    @Override
    public void setCurrentHealth(double health) {
        if (health <= 0) {
            currentHealth = 0;
            kill();
        } else {
            double maxHealth = getMaxHealth();
            this.currentHealth = Math.min(health, maxHealth); // Cap at max health

            // Update display name to show new health
            setCustomName(displayName());
        }
    }

    @Override
    public double getMaxHealth() {
        return getStatProfile().get(Statistic.HEALTH);
    }

    @Override
    public boolean isInvulnerable() {
        return isInvulnerable;
    }

    @Override
    public void setInvulnerable(boolean invulnerable) {
        this.isInvulnerable = invulnerable;
    }

    @Override
    public void attack(CombatEntity target, DamageType damageType) {
        if (target == null || target.isInvulnerable()) {
            return;
        }

        // Calculate damage based on stats
        double absoluteDamage = calculateAbsoluteDamage();

        // Create damage object using the builder pattern
        SkyblockDamage damage = SkyblockDamage.builder()
                .rawDamage(absoluteDamage)
                .criticalHit(false) // Entities don't crit by default, can be overridden in subclasses
                .damageType(damageType)
                .sourceEntity(this)
                .targetEntity(target)
                .build();

        // Apply damage to target
        target.damage(damage);

        // Apply knockback if melee
        if (damageType == DamageType.MELEE) {
            target.applyKnockback(this.getEntity());
        }
    }

    @Override
    public void damage(SkyblockDamage damage) {
        if (isInvulnerable() || isDead()) {
            return;
        }

        // Calculate actual damage after defense
        double rawDamage = damage.getRawDamage();
        double damageTaken = applyDefenseReduction(rawDamage, damage.getDamageType());

        // Spawn damage indicator
        spawnDamageIndicator(damageTaken, damage.isCriticalHit());

        // Apply damage to health
        setCurrentHealth(getCurrentHealth() - damageTaken);

        // Update display name
        setCustomName(displayName());
    }

    /**
     * Calculates the absolute damage that would be dealt by this entity
     * based on its base damage and strength stats
     *
     * @return the calculated damage value
     */
    protected double calculateAbsoluteDamage() {
        StatProfile stats = getStatProfile();
        double baseDamage = stats.get(Statistic.DAMAGE);
        double strength = stats.get(Statistic.STRENGTH);

        return (5.0 + baseDamage) * (1.0 + strength / 100.0);
    }

    /**
     * Calculates the damage reduction from defense
     *
     * @param damage the incoming damage amount
     * @param damageType the type of damage being dealt
     * @return the amount of damage after defense reduction
     */
    protected double applyDefenseReduction(double damage, DamageType damageType) {
        // If damage type bypasses defense, return full damage
        if (damageType.bypassesDefense()) {
            return damage;
        }

        double defense = getStatProfile().get(Statistic.DEFENSE);
        return damage * (1.0 - (defense / (defense + 100.0)));
    }

    @Override
    public void applyKnockback(Entity source) {
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
    public void spawnDamageIndicator(double damage, boolean isCritical) {
        // Implementation depends on your DamageIndicator system
        // Can be implemented when DamageIndicator class is available
    }

    @Override
    public void kill() {
        // Remove from active mobs list and destroy entity
        activeMobs.remove(this);
        remove();

        // Subclasses can override to handle drops, experience, etc.
    }

    /**
     * Gets the current level of this entity
     *
     * @return the entity level
     */
    public int getLevel() {
        return level;
    }

    /**
     * Checks if an entity with the specified UUID is alive
     *
     * @param uuid the entity UUID
     * @return true if the entity is alive, false otherwise
     */
    public static boolean isAlive(UUID uuid) {
        for (SkyblockEntity activeMob : activeMobs) {
            if (activeMob.getUuid().equals(uuid)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the SkyblockEntity instance with the specified UUID
     *
     * @param uuid the entity UUID
     * @return the SkyblockEntity instance, or null if not found
     */
    public static @Nullable SkyblockEntity getSkyblockInstance(UUID uuid) {
        for (SkyblockEntity activeMob : activeMobs) {
            if (activeMob.getUuid().equals(uuid)) {
                return activeMob;
            }
        }
        return null;
    }

    @Override
    public void setCustomName(@Nullable Component customName) {
        if (customName != null) {
            set(DataComponents.CUSTOM_NAME, customName);
        }
    }

    @Override
    public String toString() {
        return "SkyblockEntity{" +
                "name=" + name() +
                ", level=" + level +
                ", health=" + getCurrentHealth() + "/" + getMaxHealth() +
                ", isInvulnerable=" + isInvulnerable +
                '}';
    }
}