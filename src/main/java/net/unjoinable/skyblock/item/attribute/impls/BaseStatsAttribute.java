package net.unjoinable.skyblock.item.attribute.impls;

import net.kyori.adventure.text.Component;
import net.minestom.server.codec.Codec;
import net.minestom.server.codec.StructCodec;
import net.unjoinable.skyblock.item.attribute.AttributeContainer;
import net.unjoinable.skyblock.item.attribute.traits.ItemAttribute;
import net.unjoinable.skyblock.item.attribute.traits.LoreAttribute;
import net.unjoinable.skyblock.player.SkyblockPlayer;
import net.unjoinable.skyblock.statistic.Statistic;
import net.unjoinable.skyblock.utils.NamespaceId;
import org.jspecify.annotations.Nullable;


import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * An ItemAttribute implementation that defines base statistical values for an item.
 * This attribute holds a map of statistics and their corresponding values.
 * <p>
 * The attribute performs defensive copying of the statistics map to prevent
 * external modification after construction.
 */
public record BaseStatsAttribute(Map<Statistic, Double> baseStats) implements ItemAttribute, LoreAttribute {
    public static final NamespaceId ID = new NamespaceId("attribute", "baseStats");
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

    /**
     * Default constructor that creates an empty stats attribute.
     */
    public BaseStatsAttribute() {
        this(new EnumMap<>(Statistic.class));
    }

    @Override
    public NamespaceId id() {
        return ID;
    }

    @Override
    public Codec<? extends ItemAttribute> codec() {
        return CODEC;
    }

    @Override
    public List<Component> loreLines(@Nullable SkyblockPlayer player, AttributeContainer container) {
        return List.of();
    }

    @Override
    public int priority() {
        return 0;
    }
}