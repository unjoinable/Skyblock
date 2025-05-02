package net.skyblock.item.component.impl;

import net.skyblock.item.component.ItemComponent;
import net.skyblock.item.enums.Rarity;
import org.jetbrains.annotations.NotNull;

/**
 * A component representing the rarity of an item and its upgrade status.
 */
public record RarityComponent(@NotNull Rarity rarity, boolean isUpgraded) implements ItemComponent {

    /**
     * Creates a new RarityComponent with the specified rarity value.
     *
     * @param rarity The new rarity value
     * @return A new RarityComponent instance with the updated rarity and the same upgrade status
     */
    public @NotNull RarityComponent with(@NotNull Rarity rarity) {
        return new RarityComponent(rarity, this.isUpgraded);
    }

    /**
     * Creates a new RarityComponent with the specified upgrade status.
     *
     * @param isUpgraded The new upgrade status
     * @return A new RarityComponent instance with the updated upgrade status and the same rarity
     */
    public @NotNull RarityComponent with(boolean isUpgraded) {
        return new RarityComponent(this.rarity, isUpgraded);
    }
}