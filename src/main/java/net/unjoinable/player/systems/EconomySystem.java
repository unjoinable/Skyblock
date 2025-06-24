package net.unjoinable.player.systems;

import net.unjoinable.player.PlayerSystem;

/**
 * EconomySystem manages a player's economic resources including coins and bits.
 * This system handles balance tracking, transactions, and currency operations
 * for players within the game environment.
 * <p>
 * The system supports both positive and negative currency amounts, allowing
 * for debt scenarios and flexible economic mechanics.
 */
public class EconomySystem implements PlayerSystem {
    private boolean isInitialized;
    private long coins;
    private long bits;

    @Override
    public void start() {
        this.isInitialized = true;
    }

    @Override
    public boolean isInitialized() {
        return isInitialized;
    }

    /**
     * Sets the player's coin balance to the specified amount.
     *
     * @param coins the new coin balance to set
     */
    public void setCoins(long coins) {
        this.coins = coins;
    }

    /**
     * Sets the player's bit balance to the specified amount.
     *
     * @param bits the new bit balance to set
     */
    public void setBits(long bits) {
        this.bits = bits;
    }

    /**
     * Gets the player's current coin balance.
     *
     * @return the current coin balance
     */
    public long getCoins() {
        return coins;
    }

    /**
     * Gets the player's current bit balance.
     *
     * @return the current bit balance
     */
    public long getBits() {
        return bits;
    }

    /**
     * Adds the specified amount of coins to the player's balance.
     * Supports both positive and negative amounts for flexible transactions.
     *
     * @param amount the amount of coins to add (can be positive or negative)
     */
    public void addCoins(long amount) {
        this.coins += amount;
    }

    /**
     * Removes the specified amount of coins from the player's balance.
     * Supports both positive and negative amounts for flexible transactions.
     *
     * @param amount the amount of coins to remove (can be positive or negative)
     */
    public void removeCoins(long amount) {
        this.coins -= amount;
    }

    /**
     * Adds the specified amount of bits to the player's balance.
     * Supports both positive and negative amounts for flexible transactions.
     *
     * @param amount the amount of bits to add (can be positive or negative)
     */
    public void addBits(long amount) {
        this.bits += amount;
    }

    /**
     * Removes the specified amount of bits from the player's balance.
     * Supports both positive and negative amounts for flexible transactions.
     *
     * @param amount the amount of bits to remove (can be positive or negative)
     */
    public void removeBits(long amount) {
        this.bits -= amount;
    }

    /**
     * Checks if the player has sufficient coins for a transaction.
     *
     * @param amount the amount to check against
     * @return true if the player has at least the specified amount of coins
     */
    public boolean hasCoins(long amount) {
        return this.coins >= amount;
    }

    /**
     * Checks if the player has sufficient bits for a transaction.
     *
     * @param amount the amount to check against
     * @return true if the player has at least the specified amount of bits
     */
    public boolean hasBits(long amount) {
        return this.bits >= amount;
    }
}