package net.unjoinable.skyblock.player;

/**
 * Defines the lifecycle and behavior of a player management system.
 * <p>
 * The PlayerSystem interface represents a core game subsystem responsible for
 * managing player-related operations throughout the application lifecycle.
 * Implementations should handle system initialization, state updates, and cleanup.
 * </p>
 * <p>
 * A typical implementation lifecycle:
 * <ol>
 *   <li>{@link #start()} - Initialize system resources</li>
 *   <li>{@link #update()} - Process player state on each game tick</li>
 *   <li>{@link #shutdown()} - Perform system cleanup</li>
 * </ol>
 * </p>
 */
public interface PlayerSystem {

    /**
     * Checks whether the player system is initialized.
     *
     * @return true if the system has been initialized successfully, false otherwise.
     */
    boolean isInitialized();

    /**
     * Initializes the player system and associated resources.
     * <p>
     * This method should be called during application startup before any player
     * interactions occur. Implementations should handle:
     * <ul>
     *   <li>Initializing player caches and collections</li>
     *   <li>Setting up event listeners</li>
     *   <li>Preparing any resources required for player management</li>
     * </ul>
     * </p>
     */
    default void start() {}

    /**
     * Updates the player system state during each game tick.
     * <p>
     * This method is called on every game tick and should handle:
     * <ul>
     *   <li>Processing player state updates</li>
     *   <li>Updating player timers and cooldowns</li>
     *   <li>Handling pending player operations</li>
     *   <li>Processing player movement and interactions</li>
     * </ul>
     * Implementations should optimize this method for performance as it runs frequently.
     * </p>
     */
    default void update() {}

    /**
     * Performs cleanup when shutting down the player system.
     * <p>
     * This method should be called during application shutdown to:
     * <ul>
     *   <li>Remove event listeners</li>
     *   <li>Release resources held by the player system</li>
     *   <li>Cancel any pending player operations</li>
     * </ul>
     * Implementations should ensure all player data is properly persisted
     * before returning from this method.
     * </p>
     */
    default void shutdown() {}
}