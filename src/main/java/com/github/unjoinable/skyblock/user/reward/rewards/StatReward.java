package com.github.unjoinable.skyblock.user.reward.rewards;

import com.github.unjoinable.skyblock.statistics.Statistic;
import com.github.unjoinable.skyblock.user.SkyblockPlayer;
import com.github.unjoinable.skyblock.user.reward.Reward;
import com.github.unjoinable.skyblock.user.reward.RewardType;

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
