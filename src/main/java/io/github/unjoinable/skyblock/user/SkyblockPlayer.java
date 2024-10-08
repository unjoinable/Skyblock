package io.github.unjoinable.skyblock.user;

import io.github.unjoinable.skyblock.Skyblock;
import io.github.unjoinable.skyblock.item.SkyblockItem;
import io.github.unjoinable.skyblock.item.SkyblockItemProcessor;
import io.github.unjoinable.skyblock.user.actionbar.ActionBar;
import net.minestom.server.entity.Player;
import net.minestom.server.network.player.PlayerConnection;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;

public class SkyblockPlayer extends Player {
    private final ActionBar actionBar;
    private final StatisticsHandler statsHandler;

    public SkyblockPlayer(@NotNull UUID uuid, @NotNull String username, @NotNull PlayerConnection playerConnection) {
        super(uuid, username, playerConnection);
        actionBar = new ActionBar();
        statsHandler = new StatisticsHandler(this);

    }

    public void updateItemCache() {
        Map<ItemSlot, SkyblockItem> oldCache = PlayerItemCache.fromCache(this).getAll();
        oldCache.clear();
        SkyblockItemProcessor processor = Skyblock.getItemProcessor();
        for (ItemSlot value : ItemSlot.getValues()) {
            SkyblockItem item = processor.fromItemStack(value.get(this));
            if (item.id().equalsIgnoreCase("AIR")) continue;
            oldCache.put(value, item);
        }
    }

    public ActionBar getActionBar() {
        return actionBar;
    }

}
