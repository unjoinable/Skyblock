package io.github.unjoinable.user.actionbar;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author Swofty
 * @since 1.0.0
 */
public enum ActionBarSection {
    HEALTH,
    DEFENSE,
    MANA;

    private static final Collection<ActionBarSection> VALUES = Arrays.asList(values());

    public static Collection<ActionBarSection> getValues() {
        return VALUES;
    }
}
