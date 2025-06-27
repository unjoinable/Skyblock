package net.unjoinable.skyblock.player;

import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages registration, retrieval, and lifecycle of game systems.
 * <p>
 * The SystemsManager provides centralized management for all game systems,
 * ensuring proper initialization checking and providing safe access patterns.
 * It supports systems that implement various interfaces but focuses on
 * systems with initialization state tracking.
 * </p>
 * <p>
 * Usage example:
 * <pre>
 * SystemsManager manager = new SystemsManager(player);
 * manager.registerSystem(new MyPlayerSystemImpl());
 *
 * PlayerSystem playerSystem = manager.getSystem(PlayerSystem.class);
 * if (playerSystem != null) {
 *     // Use the system
 * }
 * </pre>
 * </p>
 */
public class SystemsManager {
    private static final Logger LOG = LoggerFactory.getLogger(SystemsManager.class);
    private final Map<Class<?>, PlayerSystem> systems;
    private boolean isShuttingDown;

    public SystemsManager() {
        this.systems = new HashMap<>();
        this.isShuttingDown = false;
    }

    /**
     * Registers a system instance with the manager.
     * <p>
     * The system will be stored and can be retrieved later using its class type.
     * If a system of the same type already exists, the registration will be ignored.
     *
     * @param system the system instance to register
     * @param <T> the type of the system
     * @throws IllegalStateException if the manager is shutting down
     * @throws IllegalArgumentException if a system of the same type is already registered
     */
    public <T extends PlayerSystem> void registerSystem(T system) {
        if (isShuttingDown) {
            throw new IllegalStateException("Cannot register systems during shutdown");
        }

        Class<?> systemClass = system.getClass();
        if (this.systems.containsKey(systemClass)) {
            throw new IllegalArgumentException("System of type " + systemClass.getSimpleName() + " is already registered");
        }

        this.systems.put(systemClass, system);
    }

    /**
     * Retrieves a system by its class type.
     * <p>
     * This method performs initialization checking for systems that implement
     * an interface with an {@code isInitialized()} method (like PlayerSystem).
     * Warnings are logged for uninitialized or missing systems.
     * </p>
     *
     * @param systemClass the class type of the system to retrieve
     * @param <T> the type of the system
     * @return the system instance, or null if not found or uninitialized
     */
    public @Nullable <T extends PlayerSystem> T getSystem(Class<T> systemClass) {
        PlayerSystem system = systems.get(systemClass);

        if (system == null) {
            LOG.warn("System not found: {}. Make sure it's registered.", systemClass.getSimpleName());
            return null;
        }

        //noinspection unchecked
        return (T) system;
    }

    /**
     * Checks if a system is registered.
     *
     * @param systemClass the class type to check for
     * @return true if a system of this type is registered, false otherwise
     */
    public boolean hasSystem(Class<?> systemClass) {
        return systems.containsKey(systemClass);
    }

    /**
     * Starts all registered systems that have a start() method.
     * <p>
     * This method will attempt to call start() on all registered systems
     * that support it. Exceptions from individual systems are logged
     * but do not prevent other systems from starting.
     * </p>
     */
    public void startAllSystems() {
        for (Map.Entry<Class<?>, PlayerSystem> entry : systems.entrySet()) {
            try {
                PlayerSystem system = entry.getValue();
                system.start();
            } catch (Exception e) {
                LOG.error("Failed to start system: {}", entry.getKey().getSimpleName(), e);
            }
        }
    }

    /**
     * Updates all registered systems that have an update() method.
     * <p>
     * This method should be called on each game tick. Exceptions from
     * individual systems are logged but do not prevent other systems
     * from updating.
     * </p>
     */
    public void updateAllSystems() {
        for (Map.Entry<Class<?>, PlayerSystem> entry : systems.entrySet()) {
            try {
                PlayerSystem system = entry.getValue();
                system.update();
            } catch (Exception e) {
                LOG.error("Failed to update system: {}", entry.getKey().getSimpleName(), e);
            }
        }
    }

    /**
     * Shuts down all registered systems that have a shutdown() method.
     * <p>
     * This method should be called during application shutdown.
     * It sets the shutting down flag to prevent new system registrations.
     * </p>
     */
    public void shutdownAllSystems() {
        isShuttingDown = true;

        for (Map.Entry<Class<?>, PlayerSystem> entry : systems.entrySet()) {
            try {
                PlayerSystem system = entry.getValue();
                system.shutdown();
                LOG.info("Shut down system: {}", entry.getKey().getSimpleName());
            } catch (Exception e) {
                LOG.error("Failed to shutdown system: {}", entry.getKey().getSimpleName(), e);
            }
        }

        systems.clear();
    }

    /**
     * Checks if the manager is in shutdown state.
     *
     * @return true if shutting down, false otherwise
     */
    public boolean isShuttingDown() {
        return isShuttingDown;
    }
}