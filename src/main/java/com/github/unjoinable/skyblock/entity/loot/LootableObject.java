package com.github.unjoinable.skyblock.entity.loot;

import com.github.unjoinable.skyblock.user.SkyblockPlayer;
import com.github.unjoinable.skyblock.util.NamespacedObject;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an object that can be looted.
 * <p>
 * This interface defines the contract for objects that can be given to a player as loot.
 */
public interface LootableObject extends NamespacedObject {

    /**
     * Gives the object to the specified player as loot.
     *
     * @param player the player to give the object to
     */
    void give(@NotNull SkyblockPlayer player);

}