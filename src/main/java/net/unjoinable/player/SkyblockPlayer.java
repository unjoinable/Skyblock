package net.unjoinable.player;

import net.minestom.server.entity.Player;
import net.minestom.server.network.player.GameProfile;
import net.minestom.server.network.player.PlayerConnection;
import net.unjoinable.player.systems.PlayerStatSystem;
import net.unjoinable.player.ui.actionbar.PlayerActionBar;
import org.jetbrains.annotations.NotNull;

/**
 * Extended Player class for Skyblock gameplay with custom systems and UI components.
 * <p>
 * This class extends Minestom's Player to provide Skyblock-specific functionality
 * including stat systems, action bar management, and invulnerability handling.
 */
public class SkyblockPlayer extends Player {
    private SystemsManager systemsManager;
    private PlayerStatSystem statSystem;
    private final PlayerActionBar actionBar;

    /**
     * Constructs a new SkyblockPlayer with the specified connection and profile.
     * <p>
     * Initializes the player with a custom action bar for displaying Skyblock-specific
     * information such as health, mana, and other stats.
     *
     * @param playerConnection the player's network connection
     * @param gameProfile the player's game profile containing UUID and username
     */
    public SkyblockPlayer(@NotNull PlayerConnection playerConnection, @NotNull GameProfile gameProfile) {
        super(playerConnection, gameProfile);
        this.actionBar = new PlayerActionBar(this);
    }

    @Override
    public boolean isInvulnerable() {
        return statSystem.isInvulnerable();
    }

    /**
     * Sets the systems manager for this player and initializes the stat system.
     * <p>
     * This method can only be called once per player instance. The systems manager
     * is responsible for managing all player-related systems, and the stat system
     * is automatically retrieved and cached for quick access.
     *
     * @param systemsManager the systems manager to set
     * @throws IllegalStateException if a systems manager has already been set
     */
    public void setSystemsManager(@NotNull SystemsManager systemsManager) {
        if (this.systemsManager != null) {
            throw new IllegalStateException("Systems manager already set");
        }
        this.systemsManager = systemsManager;
        this.statSystem = systemsManager.getSystem(PlayerStatSystem.class);
    }


    /**
     * Gets the systems manager responsible for managing this player's systems.
     * <p>
     * The systems manager coordinates all player-related systems such as stats,
     * inventory, abilities, and other gameplay mechanics.
     *
     * @return the systems manager for this player
     * @throws NullPointerException if the systems manager has not been set
     */
    public @NotNull SystemsManager getSystemsManager() {
        return systemsManager;
    }

    /**
     * Gets the player's stat system for managing health, mana, and other statistics.
     * <p>
     * This is a convenience method that provides direct access to the stat system
     * without needing to go through the systems manager.
     *
     * @return the player's stat system
     * @throws NullPointerException if the systems manager has not been set
     */
    public PlayerStatSystem getStatSystem() {
        return statSystem;
    }

    /**
     * Gets the player's action bar component for displaying custom UI information.
     * <p>
     * The action bar is used to show Skyblock-specific information such as health,
     * mana, defense, and other important stats in a user-friendly format.
     *
     * @return the player's action bar component
     */
    public PlayerActionBar getActionBar() {
        return actionBar;
    }
}