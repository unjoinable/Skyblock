package net.unjoinable.item.attribute.traits;

import net.unjoinable.item.ItemMetadata;
import net.unjoinable.item.attribute.AttributeContainer;
import net.unjoinable.item.attribute.ItemAttribute;
import net.unjoinable.statistic.StatProfile;
import org.jetbrains.annotations.NotNull;

public interface StatModifierAttribute extends ItemAttribute {

    @NotNull StatProfile modifierStats(@NotNull AttributeContainer container, @NotNull ItemMetadata metadata);

}
