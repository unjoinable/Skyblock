package com.github.unjoinable.skyblock.user.requirement.requirements;

import com.github.unjoinable.skyblock.skill.Skill;
import com.github.unjoinable.skyblock.user.SkyblockPlayer;
import com.github.unjoinable.skyblock.user.requirement.RequirementChecker;
import org.jetbrains.annotations.NotNull;

public record SkillRequirement(@NotNull Skill requiredSkill, byte requiredLvl) implements RequirementChecker {

    @Override
    public boolean meetsRequirement(@NotNull SkyblockPlayer player) {
        //add a Lvl calculator.
        return false;
    }

}
