package com.github.unjoinable.skyblock.item.component.components;

import com.github.unjoinable.skyblock.item.component.trait.SerializableComponent;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.component.HeadProfile;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Represents a component that stores a player head texture string (base64 encoded).
 * This component is {@link SerializableComponent}, allowing the texture to be saved
 * to an {@link ItemStack}'s NBT as a {@link HeadProfile}.
 */
public class HeadTextureComponent implements SerializableComponent {
    private final String texture;

    /**
     * Constructs a new HeadTextureComponent with the given texture string.
     * @param texture The base64 encoded texture string.
     */
    public HeadTextureComponent(String texture) {
        this.texture = texture;
    }

    /**
     * Creates an NBT writer that sets the {@link HeadProfile} component on the item builder
     * using the stored texture and a random UUID.
     */
    @Override
    public void nbtWriter(ItemStack.@NotNull Builder builder) {
        PlayerSkin skin = new PlayerSkin(texture, UUID.randomUUID().toString());
        HeadProfile profile = new HeadProfile(skin);
        // Set the HeadProfile component on the item
        builder.set(net.minestom.server.item.ItemComponent.PROFILE, profile);
    }

    /**
     * Gets the base64 encoded head texture string.
     * @return The texture string.
     */
    public String getTexture() {
        return texture;
    }
}