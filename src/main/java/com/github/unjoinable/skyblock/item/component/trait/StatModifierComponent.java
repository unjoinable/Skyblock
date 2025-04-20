package com.github.unjoinable.skyblock.item.component.trait;

import com.github.unjoinable.skyblock.item.enums.ModifierType;
import com.github.unjoinable.skyblock.stats.StatProfile;

/**
 * Class representing a stat modifier
 */
public interface StatModifierComponent extends TransientComponent {

    /**
     * Gets the stat profile for this modifier
     */
    StatProfile getStatProfile();

    /**
     * Gets the type of this modifier
     */
    ModifierType getModifierType();
}
