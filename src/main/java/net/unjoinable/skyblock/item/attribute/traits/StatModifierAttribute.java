package net.unjoinable.skyblock.item.attribute.traits;

import net.unjoinable.skyblock.item.ItemMetadata;
import net.unjoinable.skyblock.item.attribute.AttributeContainer;
import net.unjoinable.skyblock.player.SkyblockPlayer;
import net.unjoinable.skyblock.statistic.StatProfile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public non-sealed interface StatModifierAttribute extends ItemAttribute {

    @NotNull
    default StatProfile modifierStats(@NotNull AttributeContainer container, @NotNull ItemMetadata metadata) {
        return modifierStats(null, container, metadata);
    }

    @NotNull StatProfile modifierStats(@Nullable SkyblockPlayer player, @NotNull AttributeContainer container, @NotNull ItemMetadata metadata);

}
