package io.github.unjoinable.skyblock.user.requirement;

import io.github.unjoinable.skyblock.user.SkyblockPlayer;
import org.jetbrains.annotations.NotNull;

public interface RequirementChecker {

    boolean meetsRequirement(@NotNull SkyblockPlayer player);

}
