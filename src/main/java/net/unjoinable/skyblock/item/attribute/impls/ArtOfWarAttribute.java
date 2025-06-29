package net.unjoinable.skyblock.item.attribute.impls;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.minestom.server.codec.Codec;
import net.minestom.server.codec.StructCodec;
import net.unjoinable.skyblock.item.ItemMetadata;
import net.unjoinable.skyblock.item.attribute.AttributeContainer;
import net.unjoinable.skyblock.item.attribute.traits.ItemAttribute;
import net.unjoinable.skyblock.item.attribute.traits.StatModifierAttribute;
import net.unjoinable.skyblock.player.SkyblockPlayer;
import net.unjoinable.skyblock.statistic.StatProfile;
import net.unjoinable.skyblock.statistic.StatValueType;
import net.unjoinable.skyblock.statistic.Statistic;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

import java.util.Map;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.RED;
import static net.unjoinable.skyblock.statistic.Statistic.STRENGTH;

public record ArtOfWarAttribute(boolean applied) implements StatModifierAttribute {
    public static final Key KEY = Key.key("skyblock:art_of_war");
    public static final Codec<ArtOfWarAttribute> CODEC = StructCodec.struct(
            "applied", Codec.BOOLEAN, ArtOfWarAttribute::applied,
            ArtOfWarAttribute::new
    );

    @Override
    public StatProfile modifierStats(@Nullable SkyblockPlayer player, AttributeContainer container, ItemMetadata metadata) {
        StatProfile profile = new StatProfile();
        if (applied) profile.addStat(STRENGTH, StatValueType.BASE ,40D);
        return profile;
    }

    @Override
    public Map<Statistic, Component> display() {
        return Map.of(STRENGTH, text("[40]", RED));
    }

    @Override
    public int modifierPriority() {
        return 2;
    }

    @Override
    public boolean shouldDisplay() {
        return applied;
    }

    @Override
    public Codec<? extends ItemAttribute> codec() {
        return CODEC;
    }

    @Override
    public @NotNull Key key() {
        return KEY;
    }
}
