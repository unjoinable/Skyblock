package io.github.unjoinable.user;

import io.github.unjoinable.Skyblock;
import io.github.unjoinable.item.SkyblockItem;
import io.github.unjoinable.item.SkyblockItemProcessor;
import io.github.unjoinable.statistics.Statistic;
import io.github.unjoinable.user.actionbar.ActionBar;
import io.github.unjoinable.user.actionbar.ActionBarSection;
import net.minestom.server.entity.Player;
import net.minestom.server.network.player.PlayerConnection;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;

import static io.github.unjoinable.util.MiniMessageTemplate.MM;

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

    public void tick() {
        //handlers tick
        updateItemCache();
        statsHandler.tick();

        //action bar
        Map<Statistic, Integer> statistics = statsHandler.getStatistics();
        actionBar.setDefaultDisplay(ActionBarSection.HEALTH,
                 MM."<red>\{statsHandler.getHealth()}/\{statistics.get(Statistic.HEALTH)} ❤");
        actionBar.setDefaultDisplay(ActionBarSection.DEFENSE,
                MM."<green>\{statistics.get(Statistic.DEFENSE)} ❈ Defense");
        actionBar.setDefaultDisplay(ActionBarSection.MANA,
                MM."<aqua>\{statsHandler.getMana()}/\{statistics.get(Statistic.INTELLIGENCE)} ✎ Mana");
        this.sendActionBar(actionBar.build());
    }
}
