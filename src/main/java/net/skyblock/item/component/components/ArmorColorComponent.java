package net.skyblock.item.component.components;

import net.minestom.server.color.Color;
import net.minestom.server.component.DataComponents;
import net.minestom.server.item.ItemStack;
import net.skyblock.item.component.trait.SerializableComponent;
import org.jetbrains.annotations.NotNull;

/**
 * A component that applies a custom dye color to armor items.
 * <p>
 * This component stores RGB color values and sets the {@link DataComponents#DYED_COLOR}
 * on an {@link ItemStack} using Minestom's {@link Color} class.
 */
public class ArmorColorComponent implements SerializableComponent {
    private final int[] color;

    /**
     * Constructs a new {@code ArmorColorComponent} with the given RGB color.
     *
     * @param color An array of 3 integers representing RGB values.
     *              Must be in the range [0, 255].
     */
    public ArmorColorComponent(int[] color) {
        this.color = color;
    }

    /**
     * Constructs a new {@code ArmorColorComponent} from a comma-separated RGB string.
     * <p>
     * Example input: {@code "139,0,0"} → [139, 0, 0]
     *
     * @param color A string representing RGB values, separated by commas.
     *              Each value must be in the range 0–255.
     * @throws IllegalArgumentException if the input is malformed or values are out of range.
     */
    public ArmorColorComponent(String color) {
        this(parseColorString(color));
    }

    /**
     * Applies the dye color to the given {@link ItemStack.Builder}.
     *
     * @param builder The item stack builder to modify.
     */
    @Override
    public void write(ItemStack.@NotNull Builder builder) {
        builder.set(DataComponents.DYED_COLOR, new Color(color[0], color[1], color[2]));
    }

    /**
     * Returns the RGB color array.
     *
     * @return The color as an int array of length 3.
     */
    public int[] getColor() {
        return color;
    }

    /**
     * Converts a comma-separated RGB string (e.g. "139,0,0") into an int array.
     *
     * @param rgbString the RGB string to parse
     * @return an int array of length 3 representing [R, G, B]
     * @throws IllegalArgumentException if the input is invalid or doesn't contain exactly 3 components
     */
    private static int[] parseColorString(String rgbString) {
        String[] parts = rgbString.split(",");
        if (parts.length != 3) {
            throw new IllegalArgumentException("RGB string must contain exactly 3 comma-separated values.");
        }

        int[] color = new int[3];
        for (int i = 0; i < 3; i++) {
            try {
                color[i] = Integer.parseInt(parts[i].trim());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid number in RGB string: " + parts[i], e);
            }

            if (color[i] < 0 || color[i] > 255) {
                throw new IllegalArgumentException("RGB values must be in the range 0-255.");
            }
        }
        return color;
    }
}
