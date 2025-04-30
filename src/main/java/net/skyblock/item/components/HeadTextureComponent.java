package net.skyblock.item.components;

import net.skyblock.item.component.ItemComponent;

/**
 * Component representing a custom head texture for an item.
 * <p>
 * This component stores the Base64 encoded texture string that defines
 * the appearance of a custom player head item.
 */
public record HeadTextureComponent(String texture) implements ItemComponent {

    /**
     * Creates a new HeadTextureComponent with the specified texture.
     *
     * @param texture The Base64 encoded texture string
     * @return A new HeadTextureComponent with the updated texture
     */
    public HeadTextureComponent with(String texture) {
        return new HeadTextureComponent(texture);
    }
}