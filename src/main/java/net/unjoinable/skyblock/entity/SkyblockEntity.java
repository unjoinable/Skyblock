package net.unjoinable.skyblock.entity;

import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.minestom.server.component.DataComponents;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.*;
import net.minestom.server.entity.ai.GoalSelector;
import net.minestom.server.entity.ai.TargetSelector;
import net.minestom.server.entity.attribute.Attribute;
import net.minestom.server.event.EventDispatcher;
import net.minestom.server.event.entity.EntityDeathEvent;
import net.minestom.server.network.packet.server.play.DamageEventPacket;
import net.minestom.server.network.packet.server.play.SoundEffectPacket;
import net.minestom.server.sound.SoundEvent;
import net.minestom.server.utils.time.TimeUnit;
import net.unjoinable.skyblock.combat.damage.DamageReason;
import net.unjoinable.skyblock.combat.damage.DamageType;
import net.unjoinable.skyblock.combat.damage.SkyblockDamage;
import net.unjoinable.skyblock.combat.statistic.StatProfile;
import net.unjoinable.skyblock.combat.statistic.Statistic;
import net.unjoinable.skyblock.utils.MiniString;

import java.time.Duration;
import java.util.List;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.textOfChildren;
import static net.kyori.adventure.text.format.NamedTextColor.*;
import static net.unjoinable.skyblock.utils.NumberUtils.formatClean;

/**
 * Abstract base class for all Skyblock entities.
 */
public abstract class SkyblockEntity extends EntityCreature {
    private final int level;
    protected final StatProfile statProfile = new StatProfile();
    private boolean isInvulnerable;

    private double maxHealth;
    private double currentHealth;

    // Constants
    private static final int REMOVAL_ANIMATION_DELAY = 1000;
    private static final double BASE_DAMAGE_MODIFIER = 5.0;
    private static final double SPEED_CONVERSION_FACTOR = 1 / 1000.0;
    
    // Naming Constants
    private static final Component OPENING_BRACKET = MiniString.asComponent("<dark_gray>[");
    private static final Component CLOSING_BRACKET = MiniString.asComponent("<dark_gray>] ");
    private static final Component HEART = MiniString.asComponent("<red>❤");

    /**
     * Creates a new SkyblockEntity with the specified entity type
     *
     * @param entityType the Minestom entity type
     */
    protected SkyblockEntity(EntityType entityType, int level) {
        super(entityType);
        this.level = level;

        setCustomNameVisible(true);
        configureStats(level);
        this.maxHealth = statProfile.get(Statistic.HEALTH);
        setHealth(maxHealth);

        double speedStat = statProfile.get(Statistic.SPEED);
        getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue(speedStat * SPEED_CONVERSION_FACTOR);

        set(DataComponents.CUSTOM_NAME, displayName());
        setAutoViewable(true);
        setAutoViewEntities(true);

        // Set AI behavior
        addAIGroup(getGoalSelectors(level), getTargetSelectors(level));
    }

    /**
     * Gets the name of this entity
     *
     * @return the entity name
     */
    public abstract String name();

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
     * Generates the display name component for this entity
     *
     * @return the formatted display name
     */
    public Component displayName() {
        return textOfChildren(
                OPENING_BRACKET,
                text("Lv" + level, GRAY),
                CLOSING_BRACKET,
                text(name() + " ", RED),
                text(formatClean(getCurrentHealth()) + "/" + formatClean(maxHealth), GREEN),
                HEART
        );
    }

    /**
     * Gets the current level of this entity.
     *
     * @return the entity level
     */
    public int getLevel() {
        return level;
    }

    // Health Management

    /**
     * Gets the current health of this entity.
     *
     * @return current health value
     */
    public double getCurrentHealth() {
        return this.currentHealth;
    }

    /**
     * Gets the maximum health of this entity.
     *
     * @return maximum health value
     */
    public double getMaxHealth() {
        return maxHealth;
    }

    /**
     * Sets the current health of this entity and updates display name.
     *
     * @param health the new health value
     */
    public void setHealth(double health) {
        this.currentHealth = Math.min(health, maxHealth);

        if (this.currentHealth <= 0 && !isDead && !isInvulnerable) {
            kill();
        }

        set(DataComponents.CUSTOM_NAME, displayName());
    }

    /**
     * Sets the maximum health of this entity.
     *
     * @param maxHealth the new maximum health value
     * @throws IllegalArgumentException if maxHealth is negative or zero
     */
    public void setMaxHealth(double maxHealth) {
        if (maxHealth <= 0) {
            throw new IllegalArgumentException("Max health must be positive, got: " + maxHealth);
        }
        this.maxHealth = maxHealth;
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
    public void kill() {
        refreshIsDead(true);
        triggerStatus((byte) EntityStatuses.LivingEntity.PLAY_DEATH_SOUND);
        setPose(EntityPose.DYING);
        setHealth(0D);
        this.velocity = Vec.ZERO;

        if (hasPassenger()) {
            getPassengers().forEach(this::removePassenger);
        }
        EntityDeathEvent entityDeathEvent = new EntityDeathEvent(this);
        EventDispatcher.call(entityDeathEvent);
        scheduleRemove(Duration.of(REMOVAL_ANIMATION_DELAY, TimeUnit.MILLISECOND));
    }

    // Minestom Health System Overrides (Disabled)

    @Override
    public float getHealth() {
        throw new UnsupportedOperationException("Use getCurrentHealth() instead - Minestom health system is disabled");
    }

    @Override
    public void setHealth(float health) {
        throw new UnsupportedOperationException("Use setHealth(double) instead - Minestom health system is disabled");
    }

    // Combat System

    /**
     * Creates a melee attack damage object targeting the specified entity.
     *
     * @param target the entity to attack
     * @return configured damage object
     */
    public SkyblockDamage attackMelee(Entity target) {
        return SkyblockDamage
                .builder()
                .rawDamage(calculateAbsoluteDamage())
                .target(target)
                .damager(this)
                .damageReason(DamageReason.ENTITY)
                .damageType(DamageType.MELEE_ENTITY)
                .build();
    }

    /**
     * Applies damage to this entity with visual and audio effects.
     *
     * @param damage the damage to apply
     */
    public void damage(SkyblockDamage damage) {
        if (isInvulnerable() || isDead()) return;

        double finalDamage = applyDefenseReduction(damage.rawDamage(), damage.damageType());
        
        spawnDamageIndicator(finalDamage, damage.isCritical());
        sendDamagePacket(damage);
        applyHealthDamage(finalDamage);
        playHurtSound();
    }

    /**
     * Calculates the absolute damage that would be dealt by this entity
     * based on its base damage and strength stats.
     *
     * @return the calculated damage value
     */
    protected double calculateAbsoluteDamage() {
        double baseDamage = statProfile.get(Statistic.DAMAGE);
        double strength = statProfile.get(Statistic.STRENGTH);

        return (BASE_DAMAGE_MODIFIER + baseDamage) * (1.0 + strength / 100.0);
    }

    /**
     * Calculates the damage reduction from defense.
     *
     * @param damage the incoming damage amount
     * @param damageType the type of damage being dealt
     * @return the amount of damage after defense reduction
     */
    protected double applyDefenseReduction(double damage, DamageType damageType) {
        if (damageType.bypassesDefense()) return damage;

        double defense = statProfile.get(Statistic.DEFENSE);
        return damage * (1.0 - (defense / (defense + 100.0)));
    }

    /**
     * Spawns a damage indicator at the entity's position.
     */
    private void spawnDamageIndicator(double damage, boolean isCritical) {
        new DamageIndicator(damage, isCritical).spawn(getPosition(), getInstance());
    }

    /**
     * Sends damage packet to all viewers.
     */
    private void sendDamagePacket(SkyblockDamage damage) {
        sendPacketToViewersAndSelf(new DamageEventPacket(
                getEntityId(), 
                damage.damageType().typeId(),
                0,
                damage.damager() == null ? 0 : damage.damager().getEntityId() + 1,
                damage.damager() == null ? getPosition() : damage.damager().getPosition()
        ));
    }

    /**
     * Applies health reduction from damage.
     */
    private void applyHealthDamage(double damage) {
        setHealth(this.currentHealth - damage);
    }

    /**
     * Plays hurt sound effect to all viewers.
     */
    private void playHurtSound() {
        sendPacketToViewersAndSelf(new SoundEffectPacket(
                SoundEvent.ENTITY_GENERIC_HURT,
                Sound.Source.HOSTILE, 
                getPosition(), 
                1.0f, 1.0f, 0
        ));
    }

    @Override
    public void heal() {/*Empty to disable Minestom health operations*/}
}