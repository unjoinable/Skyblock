package io.github.unjoinable.skyblock.skill;

import io.github.unjoinable.skyblock.user.reward.Reward;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Function;


public interface SkyblockSkill {

    @NotNull String name();

    @NotNull List<Component> description();

    byte maxLevel();

    int[] levelRequirements();

    Function<Byte, Reward[]> rewards();


}
