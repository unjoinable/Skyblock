package net.unjoinable.skyblock.entity;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static net.kyori.adventure.text.format.NamedTextColor.*;

/**
 * A specialized hologram that displays damage numbers with visual effects.
 */
public class DamageIndicator extends Hologram {
    private static final TextColor[] COLORS = {WHITE, WHITE, YELLOW, GOLD, RED, RED};
    private static final Component CRIT = Component.text('✧', WHITE);
    private static final NumberFormat NUMBER_FORMAT = NumberFormat.getNumberInstance(Locale.US);
    private static final Random RANDOM = new Random();

    private final double damage;
    private final boolean criticalDamage;

    /**
     * Creates a new damage indicator.
     *
     * @param damage The damage amount to display
     * @param criticalDamage Whether this is a critical hit
     */
    public DamageIndicator(double damage, boolean criticalDamage) {
        super(Component.empty()); // Temporary empty component, will be set in constructor
        this.damage = damage;
        this.criticalDamage = criticalDamage;
        setText(formatDamageText());
    }

    /**
     * Spawns this damage indicator at the specified position with a random offset.
     *
     * @param pos The base position
     * @param instance The instance to spawn in
     */
    public void spawn(Pos pos, Instance instance) {
        setInstance(instance, randomizePosition(pos.add(0d, 1.2d, 0d)));
        scheduleRemoval(1000); // Auto-remove after 1 second
    }

    /**
     * Formats the damage value into a visually appealing component.
     *
     * @return The formatted text component
     */
    private Component formatDamageText() {
        String formattedNumber = formatNumber(damage);

        if (!criticalDamage) {
            return Component.text(formattedNumber, GRAY);
        } else {
            return CRIT.append(applyRainbowEffect(formattedNumber)).append(CRIT);
        }
    }

    /**
     * Applies rainbow coloring to each character of the text.
     *
     * @param text The text to colorize
     * @return A component with rainbow-colored characters
     */
    private Component applyRainbowEffect(String text) {
        Component colorized = Component.empty();
        int colorIndex = 0;

        for (char c : text.toCharArray()) {
            String character = String.valueOf(c);

            if (c == ',') {
                colorized = colorized.append(Component.text(character, WHITE));
                continue;
            }

            if (colorIndex >= COLORS.length) {
                colorIndex = 0;
            }

            colorized = colorized.append(Component.text(character, COLORS[colorIndex++]));
        }

        return colorized;
    }

    /**
     * Formats a number to a readable string, removing decimal places.
     *
     * @param number The number to format
     * @return The formatted number string
     */
    private static String formatNumber(double number) {
        return NUMBER_FORMAT.format(Math.round(number));
    }

    /**
     * Creates a slightly randomized position based on the input position.
     *
     * @param pos The base position
     * @return A new position with random offsets on X and Z axes
     */
    private Pos randomizePosition(Pos pos) {
        double offsetX = ThreadLocalRandom.current().nextDouble(-1, 1);
        double offsetZ = ThreadLocalRandom.current().nextDouble(-1, 1);
        return pos.add(offsetX, 0, offsetZ);
    }
}