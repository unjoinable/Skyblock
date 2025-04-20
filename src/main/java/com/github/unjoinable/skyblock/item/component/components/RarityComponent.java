package com.github.unjoinable.skyblock.item.component.components;

import com.github.unjoinable.skyblock.item.component.ComponentContainer;
import com.github.unjoinable.skyblock.item.component.trait.LoreComponent;
import com.github.unjoinable.skyblock.item.component.trait.NBTReadable;
import com.github.unjoinable.skyblock.item.component.trait.NBTWritable;
import com.github.unjoinable.skyblock.item.enums.ItemCategory;
import com.github.unjoinable.skyblock.item.enums.Rarity;
import net.kyori.adventure.text.Component;
import net.minestom.server.item.ItemStack;
import net.minestom.server.tag.Tag;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.UnaryOperator;

/**
 * A class representing the rarity component for an item, which
 * includes the rarity level and whether the rarity has been upgraded.
 */
public final class RarityComponent implements LoreComponent, NBTWritable, NBTReadable {
    private static final Tag<String> RARITY_TAG = Tag.String("rarity").defaultValue(Rarity.UNOBTAINABLE.toString());
    private static final Tag<Boolean> UPGRADED_TAG = Tag.Boolean("rarity_upgraded").defaultValue(false);

    private final Rarity rarity;
    private final boolean isUpgraded;

    /**
     * Constructs a RarityComponent with the specified rarity and upgrade status.
     *
     * @param rarity     The rarity level of the item.
     * @param isUpgraded A boolean indicating if the rarity has been upgraded.
     */
    public RarityComponent(Rarity rarity, boolean isUpgraded) {
        this.rarity = rarity;
        this.isUpgraded = isUpgraded;
    }

    /**
     * Retrieves the rarity associated with this component.
     *
     * @return The {@link Rarity} of the item.
     */
    public Rarity getRarity() {
        return rarity;
    }

    /**
     * Indicates whether the rarity is upgraded.
     *
     * @return A boolean indicating if the rarity is upgraded.
     */
    public boolean isUpgraded() {
        return isUpgraded;
    }

    @Override
    public int lorePriority() {
        return 100;
    }

    @Override
    public @NotNull List<Component> generateLore(@NotNull ComponentContainer container) {
        List<Component> lore = new ArrayList<>(1);
        ItemCategory category = ItemCategory.NONE;

        if (container.contains(ItemCategoryComponent.class)) {
            var component = container.get(ItemCategoryComponent.class);
            if (component.isPresent()) {
                category = component.get().getItemCategory();
            }
        }
        return lore;
    }

    @Override
    public @NotNull Optional<RarityComponent> fromNBT(@NotNull ItemStack itemStack) {
        return Optional.of(new RarityComponent(
                Rarity.getRarity(itemStack.getTag(RARITY_TAG)),
                itemStack.getTag(UPGRADED_TAG)
        ));
    }

    @Override
    public @NotNull UnaryOperator<ItemStack.Builder> nbtWriter() {
        return builder -> builder.set(RARITY_TAG, rarity.toString()).set(UPGRADED_TAG, isUpgraded);
    }
}