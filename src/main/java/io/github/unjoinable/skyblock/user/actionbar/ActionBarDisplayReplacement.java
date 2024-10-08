package io.github.unjoinable.skyblock.user.actionbar;

import net.kyori.adventure.text.Component;

/**
 * @author Swofty
 * @param display The component to display in the action bar.
 * @param duration The time (in ticks) the component will be replaced.
 * @param priority The priority of this replacement relative to others.
 * @param purpose The purpose of the replacement.
 * @since 1.0.0
 */
public record ActionBarDisplayReplacement(Component display,
                                          int duration,
                                          int priority,
                                          ActionBarPurpose purpose) {

}
