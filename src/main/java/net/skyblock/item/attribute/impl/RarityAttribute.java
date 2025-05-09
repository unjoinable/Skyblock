
package net.skyblock.item.attribute.impl;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.minestom.server.codec.Codec;
import net.minestom.server.codec.StructCodec;
import net.skyblock.item.attribute.AttributeContainer;
import net.skyblock.item.attribute.base.ItemAttribute;
import net.skyblock.item.attribute.base.ItemLoreAttribute;
import net.skyblock.item.attribute.base.JsonAttribute;
import net.skyblock.item.attribute.base.NbtAttribute;
import net.skyblock.item.attribute.AttributeResolver;
import net.skyblock.item.enums.Rarity;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.textOfChildren;
import static net.kyori.adventure.text.format.TextDecoration.*;

/**
 * Attribute that defines an item's rarity and upgrade status.
 * This attribute contributes to the item's lore display and persists in NBT.
 */
public record RarityAttribute(@NotNull Rarity rarity, boolean isUpgraded) implements ItemLoreAttribute, JsonAttribute, NbtAttribute {
    public static final String ID = "rarity";
    public static final Codec<RarityAttribute> CODEC = StructCodec.struct(
            "rarity", Codec.Enum(Rarity.class), RarityAttribute::rarity,
            "isUpgraded", Codec.BOOLEAN.optional(false), RarityAttribute::isUpgraded,
            RarityAttribute::new
    );

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull List<Component> loreLines(@NotNull AttributeContainer container) {
        final Rarity itemRarity = isUpgraded ? this.rarity.upgrade() : this.rarity;
        final TextColor color = itemRarity.getColor();
        final Component base = text(itemRarity.name() + " " + AttributeResolver.resolveCategory(container).name(),
                color, BOLD).decoration(ITALIC, false);

        return Collections.singletonList(isUpgraded ?
                textOfChildren(text("a ", color, OBFUSCATED), base, text(" a", color, OBFUSCATED))
                        .decoration(ITALIC, false) : base);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int priority() {
        return 100; // Displayed last in lore
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Codec<? extends ItemAttribute> getCodec() {
        return CODEC;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String id() {
        return ID;
    }
}