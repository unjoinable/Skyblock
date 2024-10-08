package io.github.unjoinable.skyblock.item.component;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ComponentContainer {
    private final Map<Class<? extends Component>, Component> components;

    public ComponentContainer() {
        this.components = new ConcurrentHashMap<>();
    }

    public Map<Class<? extends Component>, Component> getAllComponents() {
        return components;
    }

    /**
     * Add a component to the holder
     * @param component The component to add
     */
    public ComponentContainer addComponent(final @NotNull Component component) {
        components.put(component.getClass(), component);
        return this;
    }

    /**
     * Add multiple components to the holder
     * @param components The components to add
     */
    public ComponentContainer addComponents(final @NotNull Component... components) {
        for (Component component : components) addComponent(component);
        return this;
    }

    /**
     * Add multiple components to the holder
     * @param components The components to add
     */
    public ComponentContainer addComponents(final @NotNull Collection<Component> components) {
        for (Component component : components) addComponent(component);
        return this;
    }

    /**
     * Remove a component from the holder
     * @param component The component to remove
     */
    public ComponentContainer removeComponent(final @NotNull Component component) {
        components.remove(component);
        return this;
    }

    /**
     * Removes all components with the given implementation
     * @param clazz The class of the component to remove
     */
    public ComponentContainer removeComponentsByClass(final @NotNull Class<? extends Component> clazz) {
        components.remove(clazz);
        return this;
    }

    /**
     * Remove all components from the holder
     */
    public void removeAllComponents() {
        components.forEach(components::remove);
    }

    /**
     * Get all components with the given name
     * @param clazz The class of the component
     * @return A list of components with the given implementation
     */
    @SuppressWarnings("unchecked")
    public <R extends Component> R getComponent(final @NotNull Class<R> clazz) {
        return (R) components.get(clazz);
    }

    /**
     * Check if the holder has a component with the given implementation
     * @param clazz The class of the component
     * @return True if the holder has a component with the given implementation
     */
    public boolean hasComponent(final @NotNull Class<? extends Component> clazz) {
        return components.containsKey(clazz);
    }

}
