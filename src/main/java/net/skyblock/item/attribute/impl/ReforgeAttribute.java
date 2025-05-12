package net.skyblock.item.attribute.impl;

import net.kyori.adventure.text.Component;
import net.minestom.server.codec.Codec;
import net.minestom.server.codec.StructCodec;
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
    public static final String ID = "reforge";
    public static final Codec<ReforgeAttribute> CODEC = StructCodec.struct(
            "reforge", null, null,  null
    );

    /**
     * Returns the statistical modifiers provided by the reforge for the item's rarity.
     *
     * If the container includes a rarity attribute, retrieves the corresponding stat profile from the reforge.
     * Returns an empty stat profile if the rarity attribute is not present.
     *
     * @param container the attribute container holding item attributes
     * @return the stat profile for the item's rarity, or an empty profile if rarity is absent
     */
    @Override
    public @NotNull StatProfile getStats(@NotNull AttributeContainer container) {
        Optional<RarityAttribute> optRarity = container.get(RarityAttribute.class);
        return optRarity
                .map(rarityAttribute -> reforge.getStats(rarityAttribute.rarity()))
                .orElse(new StatProfile());
    }

    /**
     * Returns a formatted text component displaying the statistic value in parentheses and colored blue.
     *
     * @param stat the statistic to display
     * @param value the value of the statistic
     * @return a blue-colored component showing the formatted stat value in parentheses
     */
    @Override
    public @NotNull Component getFormattedDisplay(@NotNull Statistic stat, double value) {
        return text("(" + formatStatValue(value, stat) +")", BLUE);
    }

    /**
     * Returns the unique identifier for this attribute.
     *
     * @return the string "reforge"
     */
    @Override
    public @NotNull String id() {
        return ID;
    }

    /**
     * A codec
     *
     * @return A codec
     */
    @Override
    public @NotNull Codec<? extends ItemAttribute> getCodec() {
        return CODEC;
    }
}