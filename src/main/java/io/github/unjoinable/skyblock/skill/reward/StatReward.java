package io.github.unjoinable.skyblock.skill.reward;

import io.github.unjoinable.skyblock.enums.RewardType;
import io.github.unjoinable.skyblock.statistics.Statistic;
import io.github.unjoinable.skyblock.user.SkyblockPlayer;

public record StatReward(Statistic stat, int value) implements Reward {
    @Override
    public RewardType rewardType() {
        return RewardType.STATISTIC;
    }

    @Override
    public void give(SkyblockPlayer player) {
        //giving part I will figure out.
    }
}
