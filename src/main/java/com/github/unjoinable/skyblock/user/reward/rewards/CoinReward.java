package com.github.unjoinable.skyblock.user.reward.rewards;

import com.github.unjoinable.skyblock.user.SkyblockPlayer;
import com.github.unjoinable.skyblock.user.reward.Reward;
import com.github.unjoinable.skyblock.user.reward.RewardType;

public record CoinReward(long coins) implements Reward {

    @Override
    public RewardType rewardType() {
        return RewardType.COINS;
    }

    @Override
    public void give(SkyblockPlayer player) {
        player.addCoins(coins);
    }
}
