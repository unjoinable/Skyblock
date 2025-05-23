package net.unjoinable.item.attribute.impls;

import net.minestom.server.codec.Codec;
import net.minestom.server.codec.StructCodec;
import net.unjoinable.item.attribute.ItemAttribute;
import net.unjoinable.item.attribute.traits.CodecAttribute;
import net.unjoinable.statistic.Statistic;
import net.unjoinable.utility.NamespaceId;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * An ItemAttribute implementation that defines base statistical values for an item.
 * This attribute holds a map of statistics and their corresponding values.
 * <p>
 * The attribute performs defensive copying of the statistics map to prevent
 * external modification after construction.
 */
public record BaseStatsAttribute(@NotNull Map<Statistic, Double> baseStats) implements ItemAttribute, CodecAttribute {
    public static final NamespaceId ID = new NamespaceId("attribute", "basestats");
    public static final Codec<BaseStatsAttribute> CODEC = StructCodec.struct(
            "baseStats", Codec.Enum(Statistic.class).mapValue(Codec.DOUBLE),
            BaseStatsAttribute::baseStats,
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
        this(new HashMap<>());
    }

    @Override
    public @NotNull NamespaceId id() {
        return ID;
    }

    @Override
    public @NotNull Codec<? extends ItemAttribute> codec() {
        return CODEC;
    }
}