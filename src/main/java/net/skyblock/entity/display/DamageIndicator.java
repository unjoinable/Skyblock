package net.skyblock.entity.display;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

import static net.kyori.adventure.text.format.NamedTextColor.*;

/**
 * A specialized hologram that displays damage numbers with visual effects.
 */
public class DamageIndicator extends Hologram {
    private static final TextColor[] COLORS = {WHITE, WHITE, YELLOW, GOLD, RED, RED};
    private static final Component CRIT = Component.text('✧', WHITE);
    private static final NumberFormat NUMBER_FORMAT = NumberFormat.getNumberInstance(Locale.US);
    private static final Point OFFSET = new Pos(0d, 1.2D, 0D);
    private static final int REMOVE_DELAY = 1000;

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
        setInstance(instance, randomizePosition(pos.add(OFFSET)));
        scheduleRemoval(REMOVE_DELAY);
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
        if (text.isEmpty()) {
            return Component.empty();
        }

        final int length = text.length();
        final int colorCount = COLORS.length;
        final Component[] components = new Component[length];

        for (int i = 0; i < length; i++) {
            char c = text.charAt(i);
            TextColor color = (c == ',') ? WHITE : COLORS[i % colorCount];
            components[i] = Component.text(c, color);
        }

        Component result = Component.empty();
        for (Component component : components) {
            result = result.append(component);
        }
        return result;
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