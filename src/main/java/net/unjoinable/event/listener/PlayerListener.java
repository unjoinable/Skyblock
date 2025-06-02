package net.unjoinable.event.listener;

import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.unjoinable.player.SkyblockPlayer;
import org.jetbrains.annotations.NotNull;

/**
 * Registers and handles global player-related events for the Skyblock server.
 * <p>
 * This class listens for events such as player spawning and initializes necessary
 * systems for the player upon joining the world.
 */
public class PlayerListener {

    private final GlobalEventHandler eventHandler;

    /**
     * Constructs a new {@code PlayerListener}
     *
     * @param eventHandler the global event handler used to register event listeners
     */
    public PlayerListener(@NotNull GlobalEventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }

    /**
     * Registers all player-related event listeners with the event handler.
     */
    public void register() {
        registerPlayerSpawnListener();
    }

    /**
     * Registers a listener for {@link PlayerSpawnEvent}.
     * <p>
     * When a player spawns for the first time, this handler starts all systems associated
     * with that {@link SkyblockPlayer}.
     */
    private void registerPlayerSpawnListener() {
        this.eventHandler.addListener(PlayerSpawnEvent.class, event -> {
            SkyblockPlayer player = (SkyblockPlayer) event.getPlayer();
            player.getSystemsManager().startAllSystems();
        });
    }
}
