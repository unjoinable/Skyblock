package io.github.unjoinable.skyblock.skill.reward;

import io.github.unjoinable.skyblock.skill.RewardType;
import io.github.unjoinable.skyblock.user.SkyblockPlayer;

public record CoinReward(int coins) implements Reward {

    @Override
    public RewardType rewardType() {
        return RewardType.COINS;
    }

    @Override
    public void give(SkyblockPlayer player) {
        //just add it to his balance
    }

}
