package net.unjoinable.skyblock.item.ability;

/**
 * Defines the different types of costs that can be associated with item abilities.
 * Each cost type specifies what resource is consumed when an ability is activated.
 */
public enum AbilityCostType {

    /**
     * The ability consumes mana points from the player's mana pool.
     */
    MANA,

    /**
     * The ability consumes health points from the player's health pool.
     */
    HEALTH,

    /**
     * The ability consumes coins from the player's wallet.
     */
    COINS,

    /**
     * The ability has no cost and can be used freely.
     */
    FREE;

    /**
     * Checks if this cost type requires a numeric cost value.
     *
     * @return true if this cost type uses a value, false for FREE abilities
     */
    public boolean requiresValue() {
        return this != FREE;
    }
}
