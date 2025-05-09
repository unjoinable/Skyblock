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

    @Override
    public @NotNull Codec<? extends ItemAttribute> getCodec() {
        return CODEC;
    }

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

    @Override
    public @NotNull Component getFormattedDisplay(@NotNull Statistic stat, double value) {
        return text("(" + formatStatValue(value, stat) + ")", YELLOW);
    }

    @Override
    public @NotNull String id() {
        return ID;
    }
}
