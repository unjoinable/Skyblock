package net.skyblock.item.components;

import net.skyblock.item.component.ItemComponent;

/**
 * A record component that stores RGB color data for armor items.
 * This is typically used for leather armor coloring.
 *
 * @param color An integer array containing RGB values [red, green, blue]
 *              Each value should be in the range 0-255.
 */
public record ArmorColorComponent(int[] color) implements ItemComponent {
    /**
     * Creates a new armor color component with the specified RGB values.
     *
     * @param color An integer array containing RGB values [red, green, blue]
     * @throws IllegalArgumentException if the color array length is not 3 or contains invalid values
     */
    public ArmorColorComponent {
        if (color == null) {
            throw new IllegalArgumentException("Color array cannot be null");
        }
        if (color.length != 3) {
            throw new IllegalArgumentException("Color array must contain exactly 3 values (RGB)");
        }

        // Validate color ranges
        for (int i = 0; i < 3; i++) {
            if (color[i] < 0 || color[i] > 255) {
                throw new IllegalArgumentException("Color values must be between 0 and 255");
            }
        }
    }

    /**
     * Returns a copy of the color array to prevent external modifications.
     *
     * @return A new copy of the RGB color array
     */
    @Override
    public int[] color() {
        return color.clone();
    }
}