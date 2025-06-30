package net.unjoinable.skyblock.level;

import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.anvil.AnvilLoader;

import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages island instances in the Skyblock server.
 * Handles creation, registration, and retrieval of island world instances.
 */
public class IslandManager {
    private final Map<Island, InstanceContainer> instances;
    private final InstanceManager instanceManager;

    /**
     * Creates a new IslandManager with empty instance registry.
     */
    public IslandManager() {
        this.instances = new ConcurrentHashMap<>();
        this.instanceManager = MinecraftServer.getInstanceManager();
    }

    /**
     * Creates a new IslandManager with all default skyblock islands pre-registered.
     * 
     * @return a new IslandManager with SkyblockIsland enum values registered
     */
    public static IslandManager withDefaults() {
        IslandManager manager = new IslandManager();
        for (SkyblockIsland island : SkyblockIsland.values()) {
            manager.registerIsland(island);
        }
        return manager;
    }

    /**
     * Registers a new island by creating its world instance.
     * Sets up the instance with an Anvil chunk loader for the island's world data.
     * 
     * @param island the island to register
     */
    public void registerIsland(Island island) {
        InstanceContainer instance = instanceManager.createInstanceContainer();
        instance.setChunkLoader(new AnvilLoader(Path.of(island.worldPath())));
        instances.put(island, instance);
    }

    /**
     * Retrieves the world instance for a registered island.
     * 
     * @param island the island to get the instance for
     * @return the instance container for the island, or null if not registered
     */
    public Instance getInstance(Island island) {
        return instances.get(island);
    }
}
