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
     * Generates the lore line displaying the item's rarity and category, applying special formatting if the item is upgraded.
     *
     * @param container the attribute container used to resolve the item's category
     * @return a list containing the formatted lore component for the item's rarity and category
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
     * Returns the display priority for this attribute's lore line.
     *
     * @return 100, indicating this attribute's lore should appear last.
     */
    @Override
    public int priority() {
        return 100; // Displayed last in lore
    }

    /**
     * Returns the codec used for serializing and deserializing this attribute.
     *
     * @return the codec for this attribute type
     */
    @Override
    public @NotNull Codec<? extends ItemAttribute> getCodec() {
        return CODEC;
    }

    /**
     * Returns the unique identifier for this attribute.
     *
     * @return the string "rarity"
     */
    @Override
    public @NotNull String id() {
        return ID;
    }
}