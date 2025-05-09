package net.skyblock.item.attribute.impl;

import net.kyori.adventure.text.Component;
import net.minestom.server.codec.Codec;
import net.skyblock.item.attribute.AttributeContainer;
import net.skyblock.item.attribute.base.ItemAttribute;
import net.skyblock.item.attribute.base.StatModifierAttribute;
import net.skyblock.item.definition.Reforge;
import net.skyblock.stats.calculator.StatProfile;
import net.skyblock.stats.definition.Statistic;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.BLUE;
import static net.skyblock.utils.Utils.formatStatValue;

/**
 * Represents an item attribute granted by a {@link Reforge}.
 * This attribute provides statistical modifiers that depend on the specific reforge
 * and the item's rarity, which is obtained from the {@link AttributeContainer}.
 *
 * @param reforge The specific {@link Reforge} defining this attribute's effects.
 */
public record ReforgeAttribute(@NotNull Reforge reforge) implements StatModifierAttribute {
    private static final String ID = "reforge";

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull StatProfile getStats(@NotNull AttributeContainer container) {
        Optional<RarityAttribute> optRarity = container.get(RarityAttribute.class);
        return optRarity
                .map(rarityAttribute -> reforge.getStats(rarityAttribute.rarity()))
                .orElse(new StatProfile());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Component getFormattedDisplay(@NotNull Statistic stat, double value) {
        return text("(" + formatStatValue(value, stat) +")", BLUE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String id() {
        return ID;
    }

    @Override
    public @NotNull Codec<? extends ItemAttribute> getCodec() {
        return null;
    }
}
