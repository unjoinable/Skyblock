package io.github.unjoinable.skyblock.user.reward;

import io.github.unjoinable.skyblock.user.SkyblockPlayer;

public interface Reward {

    RewardType rewardType();

    void give(SkyblockPlayer player);

}
