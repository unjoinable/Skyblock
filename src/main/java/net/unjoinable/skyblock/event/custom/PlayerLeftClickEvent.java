package net.unjoinable.skyblock.event.custom;

import net.minestom.server.entity.Player;
import net.minestom.server.event.trait.PlayerEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.tag.Tag;

public class PlayerLeftClickEvent implements PlayerEvent {
    public static final Tag<Boolean> IGNORE_ANIMATION = Tag.Boolean("ignore_anim");
    public static final Tag<Boolean> IS_DIGGING = Tag.Boolean("is_digging");
    private final Player player;
    private final ItemStack itemStack;

    public PlayerLeftClickEvent(Player player, ItemStack itemStack) {
        this.player = player;
        this.itemStack = itemStack;
    }


    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public Player getEntity() {
        return getPlayer();
    }

    public ItemStack getItemStack() {
        return itemStack;
    }
}

