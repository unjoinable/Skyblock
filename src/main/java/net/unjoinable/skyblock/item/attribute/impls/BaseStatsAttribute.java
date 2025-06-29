package net.unjoinable.skyblock.item.attribute.impls;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.minestom.server.codec.Codec;
import net.minestom.server.codec.StructCodec;
import net.unjoinable.skyblock.item.ItemMetadata;
import net.unjoinable.skyblock.item.attribute.AttributeContainer;
import net.unjoinable.skyblock.item.attribute.traits.ItemAttribute;
import net.unjoinable.skyblock.item.attribute.traits.LoreAttribute;
import net.unjoinable.skyblock.item.service.AttributeResolver;
import net.unjoinable.skyblock.item.service.ItemStatsCalculator;
import net.unjoinable.skyblock.player.SkyblockPlayer;
import net.unjoinable.skyblock.statistic.StatProfile;
import net.unjoinable.skyblock.statistic.Statistic;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GRAY;
import static net.kyori.adventure.text.format.TextDecoration.ITALIC;

/**
 * An ItemAttribute implementation that defines base statistical values for an item.
 * This attribute holds a map of statistics and their corresponding values.
 * <p>
 * The attribute performs defensive copying of the statistics map to prevent
 * external modification after construction.
 */
public record BaseStatsAttribute(Map<Statistic, Double> baseStats) implements ItemAttribute, LoreAttribute {
    public static final Key KEY = Key.key("attribute:base_stats");
    private static final Component WHITE_SPACE = text(" ");
    public static final Codec<BaseStatsAttribute> CODEC = StructCodec.struct(
            "baseStats", Codec.Enum(Statistic.class).mapValue(Codec.DOUBLE), BaseStatsAttribute::baseStats,
            BaseStatsAttribute::new
    );

    /**
     * Constructs a new BaseStatsAttribute with the specified statistics map.
     * Creates a defensive copy of the provided map to prevent external modification.
     *
     * @param baseStats The map of statistics and their values
     */
    public BaseStatsAttribute {
        baseStats = Map.copyOf(baseStats);
    }

    @Override
    public @NotNull Key key() {
        return KEY;
    }

    @Override
    public Codec<? extends ItemAttribute> codec() {
        return CODEC;
    }

    @Override
    public List<Component> loreLines(@Nullable SkyblockPlayer player, AttributeContainer container, ItemMetadata metadata) {
        StatProfile addedStats = ItemStatsCalculator.computeItemStats(container, metadata);
        List<Component> loreLines = new ArrayList<>();

        for (Statistic stat : Statistic.values()) {
            double value = addedStats.get(stat);
            if (value == 0D) continue;

            loreLines.add(formatStat(container ,stat, value));
        }

        return loreLines;
    }

    @Override
    public int priority() {
        return 0;
    }

    // Helper method

    /**
     * Formats a single statistic and its value into a styled {@link Component}
     * for item lore display.
     * <p>
     * The value must be non-zero; this method assumes it has already been filtered out if zero.
     *
     * @param container The attribute container, used for context
     * @param stat  The statistic to format (e.g. Strength, Crit Chance)
     * @param value The non-zero value of the stat
     * @return A {@link Component} representing the stat line for display
     */
    private static Component formatStat(AttributeContainer container, Statistic stat, double value) {
        boolean isPercent = stat.isPercentage();
        String formattedValue = String.format(isPercent ? "%.1f" : "%.0f", value);

        var basic = text()
                .append(text(stat.displayName() + ": ", GRAY))
                .append(text("+" + formattedValue + (isPercent ? "%" : ""), stat.loreColor()))
                .decoration(ITALIC, false);

        AttributeResolver.getStatModifiers(container).forEach(statModifiers -> {
            if (!statModifiers.shouldDisplay()) return;
            basic.append(WHITE_SPACE).append(statModifiers.display().getOrDefault(stat, Component.empty()));
        });

        return basic.build();
    }
}