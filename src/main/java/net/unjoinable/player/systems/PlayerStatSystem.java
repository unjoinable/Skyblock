package net.unjoinable.player.systems;

import net.unjoinable.item.SkyblockItem;
import net.unjoinable.item.service.ItemProcessor;
import net.unjoinable.player.PlayerSystem;
import net.unjoinable.player.SkyblockPlayer;
import net.unjoinable.player.ui.inventory.ItemSlot;
import net.unjoinable.player.ui.inventory.VanillaItemSlot;
import net.unjoinable.statistic.StatProfile;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class PlayerStatSystem implements PlayerSystem {
    private final StatProfile baseStats;
    private final Map<ItemSlot, SkyblockItem> cachedItems;
    private final Map<ItemSlot, StatProfile> itemStats;
    private final SkyblockPlayer player;
    private final ItemProcessor itemProcessor;

    public PlayerStatSystem(@NotNull SkyblockPlayer player, @NotNull ItemProcessor itemProcessor) {
        this.cachedItems = new HashMap<>();
        this.itemProcessor = itemProcessor;
        this.player = player;
        this.baseStats = StatProfile.createDefaultProfile();
        this.itemStats = new HashMap<>();
    }

    /**
     * Updates the cached item for a specific slot
     * @param slot The slot to update
     */
    public void updateSlot(@NotNull ItemSlot slot) {
        SkyblockItem item = slot.getItem(player, itemProcessor);
        cachedItems.put(slot, item);
    }

    @Override
    public void start() {
        update();
    }

    @Override
    public void update() {
        for (VanillaItemSlot value : VanillaItemSlot.values()) {
            updateSlot(value);
        }
    }

    @Override
    public void shutdown() {
        cachedItems.clear();
    }
}
