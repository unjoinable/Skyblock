package net.skyblock.item.attribute.impl;

import net.kyori.adventure.text.Component;
import net.minestom.server.codec.Codec;
import net.minestom.server.codec.StructCodec;
import net.skyblock.item.attribute.AttributeContainer;
import net.skyblock.item.attribute.base.ItemAttribute;
import net.skyblock.item.attribute.base.StatModifierAttribute;
import net.skyblock.item.attribute.AttributeResolver;
import net.skyblock.item.enums.ItemCategory;
import net.skyblock.stats.calculator.StatProfile;
import net.skyblock.stats.definition.StatValueType;
import net.skyblock.stats.definition.Statistic;
import org.jetbrains.annotations.NotNull;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.YELLOW;
import static net.skyblock.utils.Utils.formatStatValue;

public record PotatoBookAttribute(int count) implements StatModifierAttribute {
    public static final String ID = "potato_book";
    public static final Codec<PotatoBookAttribute> CODEC = StructCodec.struct(
            "potato_book", Codec.INT, PotatoBookAttribute::count, PotatoBookAttribute::new
    );

    /**
     * Returns the codec used for serializing and deserializing this attribute.
     *
     * @return the codec for PotatoBookAttribute instances
     */
    @Override
    public @NotNull Codec<? extends ItemAttribute> getCodec() {
        return CODEC;
    }

    /**
     * Calculates and returns the stat modifications provided by this attribute based on the item's category.
     *
     * If the item is a weapon, adds base damage and strength equal to twice the count.
     * If the item is armor, adds base defense equal to twice the count and base health equal to four times the count.
     *
     * @param container the attribute container used to determine the item's category
     * @return a StatProfile containing the computed stat modifications
     */
    @Override
    public @NotNull StatProfile getStats(@NotNull AttributeContainer container) {
        StatProfile statProfile = new StatProfile();
        ItemCategory itemCategory = AttributeResolver.resolveCategory(container);

        if (itemCategory.isWeapon()) {
            statProfile.addStat(Statistic.DAMAGE, StatValueType.BASE, 2 * count);
            statProfile.addStat(Statistic.STRENGTH, StatValueType.BASE, 2 * count);
        } else if (itemCategory.isArmor()) {
            statProfile.addStat(Statistic.DEFENSE, StatValueType.BASE, 2 * count);
            statProfile.addStat(Statistic.HEALTH, StatValueType.BASE, 4 * count);
        }

        return statProfile;
    }

    /**
     * Returns a yellow-colored text component displaying the given stat value in parentheses.
     *
     * @param stat the statistic being displayed
     * @param value the value of the statistic to format
     * @return a formatted text component showing the stat value in yellow parentheses
     */
    @Override
    public @NotNull Component getFormattedDisplay(@NotNull Statistic stat, double value) {
        return text("(" + formatStatValue(value, stat) + ")", YELLOW);
    }

    /**
     * Returns the identifier string for this attribute.
     *
     * @return the attribute ID "potato_book"
     */
    @Override
    public @NotNull String id() {
        return ID;
    }
}
