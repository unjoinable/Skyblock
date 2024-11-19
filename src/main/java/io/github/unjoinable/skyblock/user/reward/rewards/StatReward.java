package io.github.unjoinable.skyblock.user.reward.rewards;

import io.github.unjoinable.skyblock.statistics.Statistic;
import io.github.unjoinable.skyblock.user.SkyblockPlayer;
import io.github.unjoinable.skyblock.user.reward.Reward;
import io.github.unjoinable.skyblock.user.reward.RewardType;

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
