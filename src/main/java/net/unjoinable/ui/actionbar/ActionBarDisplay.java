package net.unjoinable.ui.actionbar;

import net.kyori.adventure.text.Component;

/**
 * Represents a temporary replacement for a section of the action bar display.
 * <p>
 * Display replacements are prioritized and timed, allowing for dynamic content
 * management in the action bar UI. Higher priority replacements take precedence
 * over lower priority ones.
 *
 * @param display  The component to display in the action bar
 * @param duration Duration in ticks this replacement will remain active (0 for permanent)
 * @param priority Priority value determining precedence (higher values take precedence)
 * @param purpose  The functional purpose of this replacement
 */
public record ActionBarDisplay(
        Component display,
        int duration,
        int priority,
        ActionBarPurpose purpose
) {}
