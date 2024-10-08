package io.github.unjoinable.skyblock.skill.skills;

import io.github.unjoinable.skyblock.skill.SkyblockSkill;
import io.github.unjoinable.skyblock.skill.reward.Reward;
import io.github.unjoinable.skyblock.skill.reward.SkyblockXPReward;
import io.github.unjoinable.skyblock.skill.reward.StatReward;
import io.github.unjoinable.skyblock.statistics.Statistic;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Function;

public class Dungeoneering implements SkyblockSkill {
    private static final int[] TOTAL_EXP_REQUIRED = new int[]{50, 125, 235, 395, 625, 955, 1425, 2095, 3045, 4385, 6275,
    8940, 12700, 17960, 25340, 35640, 50040, 70040, 97640, 135640, 188140, 259640, 356640, 488640, 911640, 1239640,
    1684640, 2284640, 3084640, 4149640, 5559640, 7459640, 9959640, 13259640, 17559640, 23159640, 30359640, 39559640,
    51559640, 66559640, 85559640, 109559640, 139559640, 177559640, 225559640, 285559640, 360559640, 453559640, 569809640};

    @Override
    public @NotNull String name() {
        return "Dungeoneering";
    }

    @Override
    public @NotNull List<Component> description() {
        return List.of(); //todo
    }

    @Override
    public byte maxLevel() {
        return 50;
    }

    @Override
    public int[] levelRequirements() {
        return TOTAL_EXP_REQUIRED;
    }

    @Override
    public Function<Byte, Reward[]> rewards() {
        return level -> {
            SkyblockXPReward xpReward = new SkyblockXPReward(20);
            int health = 4 + Math.floorDiv(level, 5);
            StatReward statReward = new StatReward(Statistic.HEALTH, health);
            return new Reward[]{xpReward, statReward};
        };
    }
}
