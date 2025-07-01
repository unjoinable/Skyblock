package net.unjoinable.skyblock.entity;

import net.kyori.adventure.text.Component;
import net.minestom.server.component.DataComponents;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.ai.GoalSelector;
import net.minestom.server.entity.ai.TargetSelector;
import net.minestom.server.entity.attribute.Attribute;
import net.minestom.server.instance.Instance;
import net.unjoinable.skyblock.combat.damage.DamageType;
import net.unjoinable.skyblock.combat.damage.SkyblockDamage;
import net.unjoinable.skyblock.combat.statistic.StatProfile;
import net.unjoinable.skyblock.combat.statistic.Statistic;
import net.unjoinable.skyblock.utils.MiniString;

import java.util.List;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.textOfChildren;
import static net.kyori.adventure.text.format.NamedTextColor.*;

/**
 * Abstract base class for all Skyblock entities.
 */
public abstract class SkyblockEntity extends EntityCreature {
    private int level = 0;
    protected final StatProfile statProfile = new StatProfile();
    private boolean isInvulnerable = false;

    // Naming Consts
    private static final Component OPENING_BRACKET = MiniString.asComponent("<dark_gray>[");
    private static final Component CLOSING_BRACKET = MiniString.asComponent("<dark_gray>] ");
    private static final Component HEART = MiniString.asComponent("<red>❤");

    /**
     * Creates a new SkyblockEntity with the specified entity type
     *
     * @param entityType the Minestom entity type
     */
    protected SkyblockEntity(EntityType entityType) {
        super(entityType);
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
        double maxHealth = statProfile.get(Statistic.HEALTH);
        getAttribute(Attribute.MAX_HEALTH).setBaseValue(maxHealth);
        setHealth((float)maxHealth);

        // Set movement speed based on stats
        double speedStat = statProfile.get(Statistic.SPEED);
        getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue((speedStat / 1000) * 2.5);

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
        return textOfChildren(
                OPENING_BRACKET,
                text("Lv" + level, GRAY),
                CLOSING_BRACKET,
                text(name() + " ", RED),
                text(getHealth() + "/" + statProfile.get(Statistic.HEALTH), GREEN),
                HEART
        );
    }

    /**
     * Override Minestom's setHealth to update our display name when health changes
     */
    @Override
    public void setHealth(float health) {
        super.setHealth(health);

        if (health <= 0) {
            kill();
        }
        if (statProfile != null) set(DataComponents.CUSTOM_NAME, displayName());
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

    // COMBAT SPECIFIC
    /**
     * Calculates the absolute damage that would be dealt by this entity
     * based on its base damage and strength stats
     *
     * @return the calculated damage value
     */
    protected double calculateAbsoluteDamage() {
        double baseDamage = statProfile.get(Statistic.DAMAGE);
        double strength = statProfile.get(Statistic.STRENGTH);

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
        double defense = statProfile.get(Statistic.DEFENSE);
        return damage * (1.0 - (defense / (defense + 100.0)));
    }

    public SkyblockDamage attackMelee(Entity target) {
        return SkyblockDamage
                .builder()
                .rawDamage(calculateAbsoluteDamage())
                .target(target)
                .build();
    }

    public void damage(SkyblockDamage damage) {
        if (isInvulnerable() || isDead()) return;

        double damageAmount = damage.rawDamage();
        damageAmount = applyDefenseReduction(damageAmount, damage.damageType());

        setHealth(getHealth() - (float)damageAmount);
    }
}