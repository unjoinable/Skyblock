package io.github.unjoinable.skyblock.user.reward.rewards;

import io.github.unjoinable.skyblock.user.SkyblockPlayer;
import io.github.unjoinable.skyblock.user.reward.Reward;
import io.github.unjoinable.skyblock.user.reward.RewardType;

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
