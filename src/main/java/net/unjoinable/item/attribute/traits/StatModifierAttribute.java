package net.unjoinable.item.attribute.traits;

import net.unjoinable.item.ItemMetadata;
import net.unjoinable.item.attribute.AttributeContainer;
import net.unjoinable.player.SkyblockPlayer;
import net.unjoinable.statistic.StatProfile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public non-sealed interface StatModifierAttribute extends ItemAttribute {

    @NotNull
    default StatProfile modifierStats(@NotNull AttributeContainer container, @NotNull ItemMetadata metadata) {
        return modifierStats(null, container, metadata);
    }

    @NotNull StatProfile modifierStats(@Nullable SkyblockPlayer player, @NotNull AttributeContainer container, @NotNull ItemMetadata metadata);

}
