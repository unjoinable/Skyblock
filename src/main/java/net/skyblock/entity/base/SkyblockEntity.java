package net.skyblock.entity.base;

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
import net.minestom.server.entity.attribute.Attribute;
import net.minestom.server.instance.Instance;
import net.skyblock.entity.display.DamageIndicator;
import net.skyblock.stats.calculator.StatProfile;
import net.skyblock.stats.definition.DamageType;
import net.skyblock.stats.definition.SkyblockDamage;
import net.skyblock.stats.definition.Statistic;
import net.skyblock.stats.holder.CombatEntity;
import net.skyblock.utils.MiniString;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Abstract base class for all Skyblock entities.
 * Implements the CombatEntity interface to provide standard combat functionality.
 * Uses Minestom's health system internally while exposing CombatEntity interface methods.
 */
public abstract class SkyblockEntity extends EntityCreature implements CombatEntity {
    private int level = 0;
    private final StatProfile statProfile;
    private boolean isInvulnerable = false;

    /**
     * Creates a new SkyblockEntity with the specified entity type
     *
     * @param entityType the Minestom entity type
     */
    protected SkyblockEntity(@NotNull EntityType entityType) {
        super(entityType);
        this.statProfile = new StatProfile(false); // Initialize empty profile
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

        // Set max health attribute and initial health from stats
        double maxHealth = getMaxHealth();
        getAttribute(Attribute.MAX_HEALTH).setBaseValue(maxHealth);
        setHealth((float)maxHealth); // Initialize Minestom health to our max health value

        // Set movement speed based on stats
        double speedStat = getStatProfile().get(Statistic.SPEED);
        getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue((speedStat / 1000) * 2.5);

        // Set custom name and visibility settings
        set(DataComponents.CUSTOM_NAME, displayName());
        setAutoViewable(true);
        setAutoViewEntities(true);

        // Set AI behavior
        addAIGroup(getGoalSelectors(lvl), getTargetSelectors(lvl));

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
        return getHealth(); // Use Minestom's health system
    }

    @Override
    public void setCurrentHealth(double health) {
        if (health <= 0) {
            setHealth(0);
            kill();
        } else {
            double maxHealth = getMaxHealth();
            setHealth((float)Math.min(health, maxHealth)); // Cap at max health

            // Update display name to show new health
            set(DataComponents.CUSTOM_NAME, displayName());
        }
    }

    @Override
    public double getMaxHealth() {
        return getStatProfile().get(Statistic.HEALTH);
    }

    /**
     * Override Minestom's setHealth to update our display name when health changes
     */
    @Override
    public void setHealth(float health) {
        super.setHealth(health);

        // If this is a death, trigger our kill method
        if (health <= 0) {
            kill();
        }

        // Update display name to reflect new health
        set(DataComponents.CUSTOM_NAME, displayName());
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
        spawnDamageIndicator(damage);

        // Apply damage to health using our interface method
        setCurrentHealth(getCurrentHealth() - damageTaken);
    }

    /**
     * Override Minestom's default damage method to route through our SkyBlock damage system
     */

    public void damage(@NotNull net.minestom.server.entity.damage.DamageType type, float value) {
        // Convert Minestom damage to SkyBlock damage
        DamageType skyblockType = convertMinestomDamageType(type);

        // Create a SkyblockDamage object and route through our damage system
        SkyblockDamage damage = SkyblockDamage.builder()
                .rawDamage(value)
                .criticalHit(false)
                .damageType(skyblockType)
                .targetEntity(this)
                .build();

        damage(damage);
    }

    /**
     * Convert Minestom damage type to SkyBlock damage type
     *
     * @param minestomType the Minestom damage type
     * @return the equivalent SkyBlock damage type
     */
    private DamageType convertMinestomDamageType(net.minestom.server.entity.damage.DamageType minestomType) {
        // Simple mapping based on damage type name
        // This should be expanded based on your damage type enumeration
        String typeName = minestomType.toString();

        if (typeName.contains("ATTACK") || typeName.contains("PLAYER_ATTACK")) {
            return DamageType.MELEE;
        } else if (typeName.contains("PROJECTILE")) {
            return DamageType.RANGED;
        } else if (typeName.contains("MAGIC")) {
            return DamageType.MAGIC;
        } else if (typeName.contains("FIRE") || typeName.contains("LAVA")) {
            return DamageType.FIRE;
        } else if (typeName.contains("POISON")) {
            return DamageType.POISON;
        } else if (typeName.contains("VOID")) {
            return DamageType.TRUE; // Assuming TRUE is a damage type that bypasses defense
        }

        // Default to GENERIC if no match
        return DamageType.ABILITY;
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
    public void spawnDamageIndicator(SkyblockDamage damage) {
        DamageIndicator indicator = new DamageIndicator(damage.getRawDamage(), damage.isCriticalHit());
        CombatEntity source = damage.getSourceEntity();
        indicator.spawn(source.getPosition(), source.getInstance());
    }

    @Override
    public void kill() {
        remove();
    }

    /**
     * Gets the current level of this entity
     *
     * @return the entity level
     */
    public int getLevel() {
        return level;
    }
}