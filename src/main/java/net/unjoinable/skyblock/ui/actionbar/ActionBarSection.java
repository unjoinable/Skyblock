package net.unjoinable.skyblock.ui.actionbar;

import java.util.Collection;
import java.util.EnumSet;

/**
 * Represents different sections of the action bar UI.
 * Each section corresponds to a specific part of the display that can be individually controlled.
 */
public enum ActionBarSection {
    /** Player health display section */
    HEALTH,

    /** Player defense stats section */
    DEFENSE,

    /** Player mana/energy section */
    MANA;

    private static final Collection<ActionBarSection> VALUES = EnumSet.allOf(ActionBarSection.class);

    /**
     * Returns an immutable collection of all defined action bar sections.
     * This method is preferred over {@code values()} as it avoids array allocation.
     *
     * @return A collection containing all sections
     */
    public static Collection<ActionBarSection> getValues() {
        return VALUES;
    }
}
