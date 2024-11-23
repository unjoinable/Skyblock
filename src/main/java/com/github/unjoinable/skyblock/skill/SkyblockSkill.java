package com.github.unjoinable.skyblock.skill;

import com.github.unjoinable.skyblock.user.reward.Reward;
import com.github.unjoinable.skyblock.util.NamespacedObject;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Represents a skill in the Skyblock skill system.
 */
public interface SkyblockSkill extends NamespacedObject {

    /**
     * Gets the name of the skill.
     * @return The name of the skill.
     */
    @NotNull String name();

    /**
     * Gets the description of the skill.
     * @return The description of the skill.
     */
    @NotNull List<Component> description();

    /**
     * Gets the maximum level of the skill.
     * @return The maximum level of the skill.
     */
    int maxLevel();

    /**
     * Gets the level requirements for the skill.
     * @return The level requirements for the skill.
     */
    long[] levelRequirements();

    /**
     * Gets the rewards for the skill at the specified level.
     * @param level The level to get the rewards for.
     * @return The rewards for the skill at the specified level.
     */
    List<Reward> rewards(int level);

    /**
     * Gets the experience needed to reach the next level.
     * @param level The current level of the skill.
     * @return The experience needed to reach the next level.
     */
    default double getExperienceNeeded(int level) {
        long[] requirements = levelRequirements();

        if (requirements.length < level) {
            throw new IllegalArgumentException("Level " + level + " does not exist in this skill " + name());
        }

        return requirements[level];
    }
}
