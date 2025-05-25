package net.unjoinable.player.systems;

import net.unjoinable.item.SkyblockItem;
import net.unjoinable.item.service.ItemProcessor;
import net.unjoinable.item.service.ItemStatsCalculator;
import net.unjoinable.player.PlayerSystem;
import net.unjoinable.player.SkyblockPlayer;
import net.unjoinable.player.ui.inventory.ItemSlot;
import net.unjoinable.player.ui.inventory.VanillaItemSlot;
import net.unjoinable.statistic.StatProfile;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class PlayerStatSystem implements PlayerSystem {
    private StatProfile baseStats;
    private Map<ItemSlot, StatProfile> cachedItemStats;
    private final SkyblockPlayer player;
    private final ItemProcessor itemProcessor;
    private StatProfile cachedFinalStats;
    private boolean isDirty;

    private boolean isInitialized;

    public PlayerStatSystem(@NotNull SkyblockPlayer player, @NotNull ItemProcessor itemProcessor) {
        this.itemProcessor = itemProcessor;
        this.player = player;
    }

    /**
     * Updates the cached item for a specific slot
     * @param slot The slot to update
     */
    public void updateSlot(@NotNull ItemSlot slot) {
        SkyblockItem item = slot.getItem(player, itemProcessor);

        // Update Stat Cache
        StatProfile itemStats = ItemStatsCalculator.computeItemStats(item);
        cachedItemStats.put(slot, itemStats);
    }

    @Override
    public boolean isInitialized() {
        return isInitialized;
    }

    @Override
    public void start() {
        this.baseStats = StatProfile.createDefaultProfile();
        this.cachedItemStats = new HashMap<>();
        this.cachedFinalStats = new StatProfile();
        this.isDirty = true;


        for (VanillaItemSlot value : VanillaItemSlot.values()) {
            updateSlot(value);
        }
        isInitialized = true;
    }


    @Override
    public void shutdown() {
        cachedItemStats.clear();
    }
}
