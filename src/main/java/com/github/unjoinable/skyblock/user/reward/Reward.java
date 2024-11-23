package com.github.unjoinable.skyblock.user.reward;

import com.github.unjoinable.skyblock.user.SkyblockPlayer;

public interface Reward {

    RewardType rewardType();

    void give(SkyblockPlayer player);

}
