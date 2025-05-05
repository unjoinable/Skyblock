package net.skyblock;

import org.jetbrains.annotations.NotNull;
import org.tinylog.Logger;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * A dependency container that manages registration, initialization, and injection of components.
 * Directly calls the init() method on Registry objects without requiring additional interfaces.
 */
public class DependencyContainer {
    private final Map<Class<?>, ServiceDescriptor<?>> services = new ConcurrentHashMap<>();
    private final Map<Class<?>, Object> instances = new ConcurrentHashMap<>();
    private final Map<Class<?>, List<Class<?>>> dependents = new ConcurrentHashMap<>();
    private final Map<Class<?>, Set<Class<?>>> dependencies = new ConcurrentHashMap<>();
    private final Map<Class<?>, CompletableFuture<Object>> pendingRegistrations = new ConcurrentHashMap<>();

    private boolean initialized = false;
    private final List<Runnable> postInitTasks = new ArrayList<>();

    /**
     * Registers a service with its implementation factory and dependencies.
     *
     * @param serviceType The service interface or class
     * @param factory The factory that creates the service instance
     * @param dependsOn The types this service depends on
     * @param <T> The type of service
     * @return This container for chaining
     */
    public <T> DependencyContainer register(
            @NotNull Class<T> serviceType,
            @NotNull Supplier<T> factory,
            @NotNull Class<?>... dependsOn
    ) {
        checkInitialized();

        ServiceDescriptor<T> descriptor = new ServiceDescriptor<>(serviceType, factory, dependsOn);
        services.put(serviceType, descriptor);

        // Record dependencies
        Set<Class<?>> deps = new HashSet<>(Arrays.asList(dependsOn));
        dependencies.put(serviceType, deps);

        // Update dependents tracking
        for (Class<?> dependency : dependsOn) {
            dependents.computeIfAbsent(dependency, _ -> new ArrayList<>()).add(serviceType);
        }

        Logger.debug("Registered service: {}", serviceType.getSimpleName());
        return this;
    }

    /**
     * Registers a singleton instance directly.
     *
     * @param serviceType The service interface or class
     * @param instance The singleton instance
     * @param <T> The type of service
     * @return This container for chaining
     */
    public <T> DependencyContainer registerInstance(@NotNull Class<T> serviceType, @NotNull T instance) {
        checkInitialized();
        instances.put(serviceType, instance);

        // Create empty dependencies list
        dependencies.put(serviceType, new HashSet<>());

        // Mark as available for dependents
        CompletableFuture<Object> future = new CompletableFuture<>();
        future.complete(instance);
        pendingRegistrations.put(serviceType, future);

        Logger.debug("Registered instance: {}", serviceType.getSimpleName());
        return this;
    }

    /**
     * Initializes all registered services in dependency order.
     * Services are only initialized after their dependencies are initialized.
     */
    public void initialize() {
        if (initialized) {
            throw new IllegalStateException("Container is already initialized");
        }

        Logger.info("Initializing dependency container...");
        initialized = true;

        // Find all root services (no dependencies)
        List<Class<?>> initQueue = new ArrayList<>();
        for (Map.Entry<Class<?>, Set<Class<?>>> entry : dependencies.entrySet()) {
            if (entry.getValue().isEmpty() && !instances.containsKey(entry.getKey())) {
                initQueue.add(entry.getKey());
            }
        }

        // Initialize all root services
        for (Class<?> serviceType : initQueue) {
            initializeService(serviceType);
        }

        // Run post-init tasks
        Logger.info("Running {} post-initialization tasks", postInitTasks.size());
        for (Runnable task : postInitTasks) {
            task.run();
        }

        Logger.info("Dependency container initialized successfully");
    }

    /**
     * Gets a service instance, creating it if necessary.
     *
     * @param serviceType The service type to retrieve
     * @param <T> The type of service
     * @return The service instance
     * @throws IllegalStateException if the service is not registered
     */
    @SuppressWarnings("unchecked")
    public <T> T get(@NotNull Class<T> serviceType) {
        Object instance = instances.get(serviceType);
        if (instance == null) {
            if (!initialized) {
                throw new IllegalStateException("Container is not initialized yet");
            }

            ServiceDescriptor<?> descriptor = services.get(serviceType);
            if (descriptor == null) {
                throw new IllegalStateException("Service not registered: " + serviceType.getName());
            }

            // Create the instance
            instance = descriptor.factory.get();
            instances.put(serviceType, instance);

            // Complete any pending registrations
            CompletableFuture<Object> future = pendingRegistrations.get(serviceType);
            if (future != null) {
                future.complete(instance);
            }

            Logger.debug("Created instance: {}", serviceType.getSimpleName());
        }

        return (T) instance;
    }

    /**
     * Adds a task to be executed after all services are initialized.
     *
     * @param task The task to execute
     * @return This container for chaining
     */
    public DependencyContainer addPostInitTask(@NotNull Runnable task) {
        if (initialized) {
            task.run();
        } else {
            postInitTasks.add(task);
        }
        return this;
    }

    /**
     * Waits for a service to be registered and initialized.
     * Used to establish dependencies between services.
     *
     * @param serviceType The service type to wait for
     * @param <T> The type of service
     * @return A future that will be completed when the service is ready
     */
    @SuppressWarnings("unchecked")
    public <T> CompletableFuture<T> waitFor(@NotNull Class<T> serviceType) {
        // If it's already available, return immediately
        if (instances.containsKey(serviceType)) {
            return CompletableFuture.completedFuture((T) instances.get(serviceType));
        }

        // Create or get the pending registration
        CompletableFuture<Object> future = pendingRegistrations.computeIfAbsent(
                serviceType, _ -> new CompletableFuture<>());

        return future.thenApply(obj -> (T) obj);
    }

    /**
     * Initializes a service and its dependents.
     * Directly calls init() on Registry objects.
     *
     * @param serviceType The service type to initialize
     * @param <T> The type of service
     */
    @SuppressWarnings("unchecked")
    private <T> void initializeService(@NotNull Class<T> serviceType) {
        ServiceDescriptor<T> descriptor = (ServiceDescriptor<T>) services.get(serviceType);
        if (descriptor == null || instances.containsKey(serviceType)) {
            return;
        }

        Logger.debug("Initializing service: {}", serviceType.getSimpleName());

        // Create the instance
        T instance = descriptor.factory.get();
        instances.put(serviceType, instance);

        // If it's a Registry, call init()
        if (instance instanceof net.skyblock.registry.base.Registry<?, ?> registry) {
            Logger.debug("Calling init() on registry: {}", serviceType.getSimpleName());
            registry.init();
        }

        // Complete any pending registrations
        CompletableFuture<Object> future = pendingRegistrations.computeIfAbsent(
                serviceType, _ -> new CompletableFuture<>());
        future.complete(instance);

        // Initialize any dependent services
        List<Class<?>> dependentServices = dependents.getOrDefault(serviceType, Collections.emptyList());
        for (Class<?> dependentType : dependentServices) {
            // Check if all dependencies of this dependent are satisfied
            Set<Class<?>> deps = dependencies.get(dependentType);
            if (deps != null && instances.keySet().containsAll(deps)) {
                initializeService(dependentType);
            }
        }
    }

    /**
     * Ensures the container is not already initialized.
     *
     * @throws IllegalStateException if the container is already initialized
     */
    private void checkInitialized() {
        if (initialized) {
            throw new IllegalStateException("Cannot modify container after initialization");
        }
    }

    /**
         * A descriptor for a service, containing its creation factory and dependencies.
         *
         * @param <T> The type of service
         */
        private record ServiceDescriptor<T>(Class<T> serviceType, Supplier<T> factory, Class<?>[] dependencies) {
    }
}