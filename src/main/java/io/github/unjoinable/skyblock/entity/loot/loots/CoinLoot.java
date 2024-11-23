package io.github.unjoinable.skyblock.entity.loot.loots;

import io.github.unjoinable.skyblock.entity.loot.LootableObject;
import io.github.unjoinable.skyblock.user.SkyblockPlayer;
import io.github.unjoinable.skyblock.util.NamespacedId;
import org.jetbrains.annotations.NotNull;

public record CoinLoot(long coins) implements LootableObject {
    private static final NamespacedId ID = new NamespacedId("loot", "coin");

    @Override
    public @NotNull NamespacedId id() {
        return ID;
    }

    @Override
    public void give(@NotNull SkyblockPlayer player) {
        player.addCoins(coins);
    }
}
