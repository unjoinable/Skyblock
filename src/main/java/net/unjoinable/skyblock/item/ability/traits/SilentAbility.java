package net.unjoinable.skyblock.item.ability.traits;

import net.kyori.adventure.text.Component;
import net.unjoinable.skyblock.item.ability.ItemAbility;

import java.util.List;

/**
 * A trait for abilities that execute without displaying any UI elements.
 *
 * <p>Silent abilities are completely hidden from players - they have no display name,
 * no description, and don't show any visual feedback when activated. This is useful
 * for internal mechanics or passive effects that should be invisible to users.
 */
public interface SilentAbility extends ItemAbility {

    /**
     * Returns an empty description list since silent abilities are not displayed.
     *
     * @return empty list
     */
    @Override
    default List<Component> description() {
        return List.of();
    }

    /**
     * Returns an empty display name since silent abilities are not displayed.
     *
     * @return empty string
     */
    @Override
    default String displayName() {
        return "";
    }
}
