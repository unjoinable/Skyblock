package io.github.unjoinable.skyblock.user;

import io.github.unjoinable.skyblock.item.SkyblockItem;
import io.github.unjoinable.skyblock.skill.Skill;
import io.github.unjoinable.skyblock.user.actionbar.ActionBar;
import io.github.unjoinable.skyblock.util.NamespacedId;
import net.minestom.server.entity.Player;
import net.minestom.server.network.player.PlayerConnection;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

public class SkyblockPlayer extends Player {
    private final ActionBar actionBar;
    private final StatisticsHandler statsHandler;

    //player data
    private long coins = 0;
    private long bits = 0;
    private Map<Skill, Long> skills = new EnumMap<>(Skill.class);


    public SkyblockPlayer(@NotNull UUID uuid, @NotNull String username, @NotNull PlayerConnection playerConnection) {
        super(uuid, username, playerConnection);
        actionBar = new ActionBar();
        statsHandler = new StatisticsHandler(this);

    }

    /**
     * Updates the player's item cache.
     * This method clears the existing cache and repopulates it with the current items in the player's inventory.
     * Items with the ID "AIR" are ignored and not added to the cache.
     */
    public void updateItemCache() {
        Map<ItemSlot, SkyblockItem> oldCache = PlayerItemCache.fromCache(this).getAll();
        oldCache.clear();
        for (ItemSlot value : ItemSlot.getValues()) {
            SkyblockItem item = SkyblockItem.fromItemStack(value.get(this));
            if (item.id().equals(NamespacedId.AIR)) continue;
            oldCache.put(value, item);
        }
    }

    //getters

    public long getBits() {
        return bits;
    }

    public long getCoins() {
        return coins;
    }

    public @NotNull Map<Skill, Long> getAllSkillsXP() {
        return skills;
    }

    public long getSkillXP(@NotNull Skill skill) {
        return skills.getOrDefault(skill, 0L);
    }

    public ActionBar getActionBar() {
        return actionBar;
    }

    //setters

    public void setBits(long bits) {
        this.bits = bits;
    }

    public void setCoins(long coins) {
        this.coins = coins;
    }

    public void setSkillXP(@NotNull Skill skill, long value) {
        this.skills.put(skill, value);
    }

}
