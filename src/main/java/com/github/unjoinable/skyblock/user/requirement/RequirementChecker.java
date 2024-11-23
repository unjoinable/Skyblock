package com.github.unjoinable.skyblock.user.requirement;

import com.github.unjoinable.skyblock.user.SkyblockPlayer;
import org.jetbrains.annotations.NotNull;

public interface RequirementChecker {

    boolean meetsRequirement(@NotNull SkyblockPlayer player);

}
