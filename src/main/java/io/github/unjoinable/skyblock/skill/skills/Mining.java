package io.github.unjoinable.skyblock.skill.skills;

import io.github.unjoinable.skyblock.skill.SkyblockSkill;
import io.github.unjoinable.skyblock.user.reward.Reward;
import io.github.unjoinable.skyblock.util.NamespacedId;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Mining implements SkyblockSkill {
    private static final NamespacedId ID = new NamespacedId("skill", "mining");

    @Override
    public @NotNull String name() {
        return "Mining";
    }

    @Override
    public @NotNull List<Component> description() {
        return List.of();
    }

    @Override
    public int maxLevel() {
        return 60;
    }

    @Override
    public long[] levelRequirements() {
        return new long[0];
    }

    @Override
    public List<Reward> rewards(int level) {
        return List.of();
    }

    @Override
    public @NotNull NamespacedId id() {
        return ID;
    }
}
