package net.unjoinable.skyblock.item.ability;

/**
 * Defines the different execution methods for activating an item ability.
 * Each execution type corresponds to a specific player action.
 */
public enum ExecutionType {

    /**
     * The ability is triggered when the player left-clicks with the item.
     */
    LEFT_CLICK,

    /**
     * The ability is triggered when the player right-clicks with the item.
     */
    RIGHT_CLICK,

    /**
     * The ability is triggered when the player is sneaking (holding the sneak key).
     */
    SNEAK,

    /**
     * The ability is passive and does not require any manual action to activate.
     */
    PASSIVE,
    ;
}
