package com.github.unjoinable.skyblock.skill.skills;

import com.github.unjoinable.skyblock.skill.SkyblockSkill;
import com.github.unjoinable.skyblock.user.reward.Reward;
import com.github.unjoinable.skyblock.util.NamespacedId;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Dungeoneering implements SkyblockSkill {
    private static final long[] LEVEL_REQUIREMENTS = new long[]{50, 125, 235, 395, 625, 955, 1425, 2095, 3045, 4385, 6275,
    8940, 12700, 17960, 25340, 35640, 50040, 70040, 97640, 135640, 188140, 259640, 356640, 488640, 911640, 1239640,
    1684640, 2284640, 3084640, 4149640, 5559640, 7459640, 9959640, 13259640, 17559640, 23159640, 30359640, 39559640,
    51559640, 66559640, 85559640, 109559640, 139559640, 177559640, 225559640, 285559640, 360559640, 453559640, 569809640};

    private static final NamespacedId ID = new NamespacedId("skill", "dungeoneering");

    @Override
    public @NotNull String name() {
        return "Dungeoneering";
    }

    @Override
    public @NotNull List<Component> description() {
        return List.of(); //TODO: will add later
    }

    @Override
    public int maxLevel() {
        return 50;
    }

    @Override
    public long[] levelRequirements() {
        return LEVEL_REQUIREMENTS;
    }

    @Override
    public List<Reward> rewards(int level) {
        return List.of(); //TODO: will add later
    }

    @Override
    public @NotNull NamespacedId id() {
        return ID;
    }
}
