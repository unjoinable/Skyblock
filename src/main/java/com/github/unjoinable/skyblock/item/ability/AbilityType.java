package com.github.unjoinable.skyblock.item.ability;

/**
 * Represents different types of abilities that can be assigned to items in the Skyblock game.
 * Each enum constant represents a specific type of ability.
 */
public enum AbilityType {
    /**
     * Ability triggered when the player right-clicks with the item.
     */
    RIGHT_CLICK,

    /**
     * Ability triggered when the player left-clicks with the item.
     */
    LEFT_CLICK,

    /**
     * Passive ability that is always active when the item is equipped.
     */
    PASSIVE,

    /**
     * Ability triggered when the player is sneaking while holding the item.
     */
    SNEAK,

    /**
     * Bonus ability that is applied when the player has a full set of items with this ability.
     */
    FULL_SET_BONUS,
    ;

    /**
     * Overrides the default toString() method to replace underscores with spaces in the enum constant names.
     *
     * @return The enum constant name with underscores replaced by spaces.
     */
    @Override
    public String toString() {
        return super.toString().replace("_", " ");
    }
}
