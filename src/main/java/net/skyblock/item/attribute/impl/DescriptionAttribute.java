package net.skyblock.item.attribute.impl;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.codec.Codec;
import net.minestom.server.codec.StructCodec;
import net.skyblock.item.attribute.AttributeContainer;
import net.skyblock.item.attribute.base.ItemAttribute;
import net.skyblock.item.attribute.base.ItemLoreAttribute;
import net.skyblock.item.attribute.base.JsonAttribute;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static net.kyori.adventure.text.format.TextDecoration.ITALIC;

/**
 * An attribute that represents descriptive text to be displayed in an item's lore.
 * This attribute handles storing and retrieving text components used for item descriptions.
 */
public record DescriptionAttribute(@NotNull List<Component> description) implements ItemLoreAttribute, JsonAttribute {
    public static final String ID = "description";

    private static final MiniMessage miniMessage = MiniMessage
            .builder().postProcessor(component -> component.decoration(ITALIC, false)).build();

    public static final Codec<DescriptionAttribute> CODEC = StructCodec.struct(
            "description",
            Codec.STRING.transform(miniMessage::deserialize, miniMessage::serialize).list(),
            DescriptionAttribute::description,
            DescriptionAttribute::new
    );

    /**
     * Creates a new DescriptionAttribute with the given description components.
     * A defensive copy of the description list is made to prevent external modification.
     *
     * @param description The list of components making up the description
     */
    public DescriptionAttribute {
        description = new ArrayList<>(description); // Defensive Copy
    }

    /**
     * Retrieves the description components for this attribute as a new list.
     *
     * @return a defensive copy of the description components
     */
    @Override
    public @NotNull List<Component> description() {
        return new ArrayList<>(description);
    }

    /**
     * Returns the codec used to serialize and deserialize this attribute.
     *
     * @return the codec for DescriptionAttribute instances
     */
    @Override
    public @NotNull Codec<? extends ItemAttribute> getCodec() {
        return CODEC;
    }

    /**
     * Returns a defensive copy of the description components to be used as lore lines.
     *
     * @param container the attribute container (unused)
     * @return a new list containing the description components
     */
    @Override
    public @NotNull List<Component> loreLines(@NotNull AttributeContainer container) {
        return new ArrayList<>(description);
    }

    /**
     * Returns the priority value for this attribute.
     *
     * @return the integer priority, which is 10
     */
    @Override
    public int priority() {
        return 10;
    }

    /**
     * Returns the unique identifier for this attribute.
     *
     * @return the string "description"
     */
    @Override
    public @NotNull String id() {
        return ID;
    }
}