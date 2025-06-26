package net.unjoinable.skyblock.item.attribute.traits;

import net.unjoinable.skyblock.item.ItemMetadata;
import net.unjoinable.skyblock.item.attribute.AttributeContainer;
import net.unjoinable.skyblock.player.SkyblockPlayer;
import net.unjoinable.skyblock.statistic.StatProfile;
import org.jspecify.annotations.Nullable;

public interface StatModifierAttribute extends ItemAttribute {

    default StatProfile modifierStats(AttributeContainer container, ItemMetadata metadata) {
        return modifierStats(null, container, metadata);
    }

    StatProfile modifierStats(@Nullable SkyblockPlayer player, AttributeContainer container, ItemMetadata metadata);

}
