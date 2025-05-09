package net.skyblock.item.attribute.impl;

import net.minestom.server.codec.Codec;
import net.minestom.server.codec.StructCodec;
import net.minestom.server.component.DataComponents;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.component.HeadProfile;
import net.skyblock.item.attribute.AttributeContainer;
import net.skyblock.item.attribute.base.ItemAttribute;
import net.skyblock.item.attribute.base.JsonAttribute;
import net.skyblock.item.attribute.base.StackAttribute;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Represents an attribute that applies a custom texture to player head items.
 * This attribute uses the player skin system to apply textures to head items,
 * creating custom head blocks/items with specified textures.
 */
public record HeadTextureAttribute(@NotNull String texture) implements StackAttribute, JsonAttribute {
    public static final String ID = "head_texture";
    public static final Codec<HeadTextureAttribute> CODEC = StructCodec.struct(
            "texture", Codec.STRING, HeadTextureAttribute::texture,
            HeadTextureAttribute::new
    );

    /**
     * Applies the custom head texture to the provided item stack builder by setting a player skin profile based on the stored texture string.
     *
     * @param builder the item stack builder to modify
     * @param container the attribute container (unused in this method)
     */
    @Override
    public void applyToBuilder(ItemStack.@NotNull Builder builder, @NotNull AttributeContainer container) {
        PlayerSkin skin = new PlayerSkin(texture, UUID.randomUUID().toString());
        HeadProfile profile = new HeadProfile(skin);
        builder.set(DataComponents.PROFILE, profile);
    }

    /**
     * Returns the unique identifier for this attribute.
     *
     * @return the string "head_texture"
     */
    @Override
    public @NotNull String id() {
        return ID;
    }

    /**
     * Returns the codec used for serializing and deserializing this head texture attribute.
     *
     * @return the codec for this attribute
     */
    @Override
    public @NotNull Codec<? extends ItemAttribute> getCodec() {
        return CODEC;
    }
}