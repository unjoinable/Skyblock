package io.github.unjoinable.skyblock.skill.reward;

import io.github.unjoinable.skyblock.enums.RewardType;
import io.github.unjoinable.skyblock.user.SkyblockPlayer;

public interface Reward {

    RewardType rewardType();

    void give(SkyblockPlayer player);

}
