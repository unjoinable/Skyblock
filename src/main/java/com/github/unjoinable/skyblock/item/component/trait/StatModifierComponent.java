package com.github.unjoinable.skyblock.item.component.trait;

import com.github.unjoinable.skyblock.item.component.Component;
import com.github.unjoinable.skyblock.item.enums.ModifierType;
import com.github.unjoinable.skyblock.stats.StatProfile;
import org.jetbrains.annotations.NotNull;

/**
 * Interface for components that modify item stats.
 */
public interface StatModifierComponent extends Component {
    /**
     * Gets the type of this stat modifier.
     * @return The modifier type
     */
    @NotNull ModifierType getModifierType();

    /**
     * Gets the stat profile containing the modifications.
     * @return The stat profile with modifications
     */
    @NotNull StatProfile getStatProfile();
}