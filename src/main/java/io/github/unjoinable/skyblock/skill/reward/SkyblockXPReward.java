package io.github.unjoinable.skyblock.skill.reward;

import io.github.unjoinable.skyblock.skill.RewardType;
import io.github.unjoinable.skyblock.user.SkyblockPlayer;

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
