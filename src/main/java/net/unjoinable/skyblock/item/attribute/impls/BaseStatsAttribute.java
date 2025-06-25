package net.unjoinable.skyblock.item.attribute.impls;

import net.minestom.server.codec.Codec;
import net.minestom.server.codec.StructCodec;
import net.unjoinable.skyblock.item.attribute.traits.ItemAttribute;
import net.unjoinable.skyblock.item.attribute.traits.CodecAttribute;
import net.unjoinable.skyblock.statistic.Statistic;
import net.unjoinable.skyblock.utility.NamespaceId;


import java.util.EnumMap;
import java.util.Map;

/**
 * An ItemAttribute implementation that defines base statistical values for an item.
 * This attribute holds a map of statistics and their corresponding values.
 * <p>
 * The attribute performs defensive copying of the statistics map to prevent
 * external modification after construction.
 */
public record BaseStatsAttribute(Map<Statistic, Double> baseStats) implements CodecAttribute {
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
}