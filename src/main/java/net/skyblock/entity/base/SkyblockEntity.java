package net.skyblock.entity.base;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.minestom.server.component.DataComponents;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.ai.GoalSelector;
import net.minestom.server.entity.ai.TargetSelector;
import net.minestom.server.entity.attribute.Attribute;
import net.minestom.server.instance.Instance;
import net.skyblock.stats.calculator.StatProfile;
import net.skyblock.stats.definition.Statistic;
import net.skyblock.utils.MiniString;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Abstract base class for all Skyblock entities.
 */
public abstract class SkyblockEntity extends EntityCreature {
    private int level;
    private final StatProfile statProfile = new StatProfile();

    /**
     * Creates a new SkyblockEntity with the specified entity type
     */
    protected SkyblockEntity(@NotNull EntityType entityType) {
        super(entityType);
    }

    /**
     * Entity name (displayed to players)
     */
    public abstract @NotNull String name();

    /**
     * Get AI goal selectors for this entity at the specified level
     */
    public abstract List<GoalSelector> getGoalSelectors(int level);

    /**
     * Get AI target selectors for this entity at the specified level
     */
    public abstract List<TargetSelector> getTargetSelectors(int level);

    /**
     * Configure the stats based on entity level
     */
    protected abstract void configureStats(int level);

    /**
     * Spawn entity in the specified instance and position
     */
    public void spawn(int level, Instance instance, Pos position) {
        this.level = level;

        // Configure stats and attributes
        configureStats(level);
        double maxHealth = getAttribute(Attribute.MAX_HEALTH).getBaseValue();

        // Apply entity settings
        setCustomNameVisible(true);
        setHealth((float)maxHealth);
        getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue((statProfile.get(Statistic.SPEED) / 1000) * 2.5);
        set(DataComponents.CUSTOM_NAME, displayName());

        // Configure AI and visibility
        setAutoViewable(true);
        setAutoViewEntities(true);
        addAIGroup(getGoalSelectors(level), getTargetSelectors(level));

        // Spawn the entity
        setInstance(instance, position);
    }

    /**
     * Generate the formatted display name with level and health
     */
    public Component displayName() {
        return MiniString.component(
                "<dark_gray>[<gray>Lv<lvl></gray>] <red><name> <green><cur_health:'#'>/<total_health:'#'></green>❤",
                Placeholder.parsed("lvl", String.valueOf(level)),
                Placeholder.parsed("name", name()),
                Formatter.number("cur_health", getHealth()),
                Formatter.number("total_health", getAttribute(Attribute.MAX_HEALTH).getBaseValue())
        );
    }

    @Override
    public void setHealth(float health) {
        super.setHealth(health);

        if (health <= 0) {
            kill();
        }

        set(DataComponents.CUSTOM_NAME, displayName());
    }

    /**
     * Get the current level of this entity
     */
    public int getLevel() {
        return level;
    }

    /**
     * Get the entity's stat profile
     */
    protected StatProfile getStatProfile() {
        return statProfile;
    }
}