package com.github.unjoinable.skyblock.user.reward.rewards;

import com.github.unjoinable.skyblock.user.SkyblockPlayer;
import com.github.unjoinable.skyblock.user.reward.Reward;
import com.github.unjoinable.skyblock.user.reward.RewardType;

public record SkyblockXPReward(int xp) implements Reward {
    @Override
    public RewardType rewardType() {
        return RewardType.SKYBLOCK_EXP;
    }

    @Override
    public void give(SkyblockPlayer player) {
        //will add later
    }
}
