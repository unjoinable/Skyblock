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
     * {@inheritDoc}
     */
    @Override
    public void applyToBuilder(ItemStack.@NotNull Builder builder, @NotNull AttributeContainer container) {
        PlayerSkin skin = new PlayerSkin(texture, UUID.randomUUID().toString());
        HeadProfile profile = new HeadProfile(skin);
        builder.set(DataComponents.PROFILE, profile);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String id() {
        return ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Codec<? extends ItemAttribute> getCodec() {
        return CODEC;
    }
}