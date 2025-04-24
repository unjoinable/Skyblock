package net.skyblock.ui.actionbar;

/**
 * Defines the purpose/source of an action bar display replacement.
 * Used to categorize and manage different types of action bar messages.
 */
public enum ActionBarPurpose {
    /** Ability activation or cooldown notifications */
    ABILITY,

    /** Skill-related messages and progress */
    SKILL,

    /** Collection progress or milestone messages */
    COLLECTION,

    /** Music-related notifications or controls */
    MUSIC,

    /** Enchantment-related messages (e.g., Scavenger) */
    ENCHANTMENT
}