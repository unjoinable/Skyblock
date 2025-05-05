package net.skyblock;

import org.jetbrains.annotations.NotNull;
import org.tinylog.Logger;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * A streamlined dependency container that manages registration and injection of components
 * with automatic constructor-based dependency resolution.
 */
public class DependencyContainer {
    private final Map<Class<?>, Supplier<?>> factories = new ConcurrentHashMap<>();
    private final Map<Class<?>, Object> instances = new ConcurrentHashMap<>();
    private final Set<Class<?>> initializing = new HashSet<>();
    private boolean initialized = false;

    /**
     * Registers a service with its implementation factory.
     *
     * @param serviceType The service interface or class
     * @param factory The factory that creates the service instance
     * @param <T> The type of service
     */
    public <T> void register(
            @NotNull Class<T> serviceType,
            @NotNull Supplier<T> factory
    ) {
        checkNotInitialized();
        factories.put(serviceType, factory);
        Logger.debug("Registered service: {}", serviceType.getSimpleName());
    }

    /**
     * Registers a singleton instance directly.
     *
     * @param serviceType The service interface or class
     * @param instance The singleton instance
     * @param <T> The type of service
     */
    public <T> void registerInstance(@NotNull Class<T> serviceType, @NotNull T instance) {
        instances.put(serviceType, instance);
        Logger.debug("Registered instance: {}", serviceType.getSimpleName());
    }

    /**
     * Gets a service instance, creating it if necessary with automatic dependency resolution.
     *
     * @param serviceType The service type to retrieve
     * @param <T> The type of service
     * @return The service instance
     * @throws IllegalStateException if the service cannot be created
     */
    @SuppressWarnings("unchecked")
    public <T> T get(@NotNull Class<T> serviceType) {
        // Return existing instance if available
        Object instance = instances.get(serviceType);
        if (instance != null) {
            return (T) instance;
        }

        // Check for circular dependencies
        if (initializing.contains(serviceType)) {
            throw new IllegalStateException("Circular dependency detected for " + serviceType.getName());
        }

        // Create instance using registered factory or reflection
        initializing.add(serviceType);
        try {
            // Try creating from factory
            Supplier<?> factory = factories.get(serviceType);
            if (factory != null) {
                instance = factory.get();
            } else {
                // Try reflection as fallback
                instance = createInstanceUsingReflection(serviceType);
                if (instance == null) {
                    throw new IllegalStateException("Cannot create instance of " + serviceType.getName());
                }
            }

            // Store instance and initialize if needed
            instances.put(serviceType, instance);

            // Call init() if it's a Registry
            if (instance instanceof net.skyblock.registry.base.Registry<?, ?> registry) {
                registry.init();
            }

            return (T) instance;
        } finally {
            initializing.remove(serviceType);
        }
    }

    /**
     * Attempts to create an instance using reflection by finding the constructor with
     * the most parameters that can be satisfied.
     *
     * @param classType The class to instantiate
     * @param <T> The type of class
     * @return The created instance or null if no suitable constructor was found
     */
    @SuppressWarnings("unchecked")
    private <T> T createInstanceUsingReflection(Class<T> classType) {
        try {
            Constructor<?>[] constructors = classType.getConstructors();
            Arrays.sort(constructors, Comparator.comparing(Constructor::getParameterCount, Comparator.reverseOrder()));

            for (Constructor<?> constructor : constructors) {
                Class<?>[] paramTypes = constructor.getParameterTypes();
                Object[] args = new Object[paramTypes.length];
                boolean canSatisfy = true;

                for (int i = 0; i < paramTypes.length; i++) {
                    try {
                        args[i] = get(paramTypes[i]); // Recursive dependency resolution
                    } catch (Exception _) {
                        canSatisfy = false;
                        break;
                    }
                }

                if (canSatisfy) {
                    return (T) constructor.newInstance(args);
                }
            }

            // Fallback to no-arg constructor
            return classType.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            Logger.error(e, "Failed to instantiate {} using reflection", classType.getSimpleName());
            return null;
        }
    }

    /**
     * Initializes all registered services.
     */
    public void initialize() {
        if (initialized) {
            throw new IllegalStateException("Container is already initialized");
        }

        Logger.info("Initializing dependency container...");
        initialized = true;

        // Initialize all registered services
        for (Class<?> serviceType : new ArrayList<>(factories.keySet())) {
            get(serviceType);
        }

        Logger.info("Dependency container initialized successfully");
    }

    /**
     * Ensures the container is not already initialized.
     *
     * @throws IllegalStateException if the container is already initialized
     */
    private void checkNotInitialized() {
        if (initialized) {
            throw new IllegalStateException("Cannot modify container after initialization");
        }
    }
}