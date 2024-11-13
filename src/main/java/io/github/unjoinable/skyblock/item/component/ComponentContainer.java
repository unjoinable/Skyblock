package io.github.unjoinable.skyblock.item.component;

import io.github.unjoinable.skyblock.item.component.components.StatisticsComponent;
import io.github.unjoinable.skyblock.statistics.StatModifier;
import io.github.unjoinable.skyblock.statistics.StatModifiers;
import io.github.unjoinable.skyblock.statistics.Statistic;
import org.jetbrains.annotations.NotNull;

import java.util.*;
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
        if (component instanceof StatComponent) {
            addStatisticComponent((StatComponent) component);
        }
        components.put(component.getClass(), component);
        return this;
    }

    /**
     * Add multiple components to the holder
     * @param components The components to add
     */
    public ComponentContainer addComponents(final @NotNull Component... components) {
        for (Component component : components) {
            if (component instanceof StatComponent) {
                addStatisticComponent((StatComponent) component);
            }
            addComponent(component);
        }
        return this;
    }

    /**
     * Add multiple components to the holder
     * @param components The components to add
     */
    public ComponentContainer addComponents(final @NotNull Collection<Component> components) {
        for (Component component : components) {
            if (component instanceof StatComponent) {
                addStatisticComponent((StatComponent) component);
            }
            addComponent(component);
        }
        return this;
    }

    /**
     * Remove a component from the holder
     * @param component The component to remove
     */
    public ComponentContainer removeComponent(final @NotNull Component component) {
        if (component instanceof StatComponent) {
            removeOldStatisticComponent((StatComponent) component);
        }
        components.remove(component);
        return this;
    }

    /**
     * Removes all components with the given implementation
     * @param clazz The class of the component to remove
     */
    @Deprecated
    public ComponentContainer removeComponentsByClass(final @NotNull Class<? extends Component> clazz) {
        components.remove(clazz);
        return this;
    }

    /**
     * Remove all components from the holder
     */
    @Deprecated
    public void removeAllComponents() {
        components.forEach(components::remove);
    }

    /**
     * Get all components with the given name
     * @param clazz The class of the component
     * @return A list of components with the given implementation
     */
    @SuppressWarnings("unchecked")
    public <T extends Component> T getComponent(final @NotNull Class<T> clazz) {
        return (T) components.get(clazz);
    }

    /**
     * Check if the holder has a component with the given implementation
     * @param clazz The class of the component
     * @return True if the holder has a component with the given implementation
     */
    public boolean hasComponent(final @NotNull Class<? extends Component> clazz) {
        return components.containsKey(clazz);
    }

    public void addStatisticComponent(StatComponent component) {
        if (!hasComponent(StatisticsComponent.class)) {
            addComponent(new StatisticsComponent(new EnumMap<>(Statistic.class)));
        }
        StatisticsComponent statComponent = getComponent(StatisticsComponent.class);
        Map<Statistic, StatModifiers> statistics = statComponent.statistics();

        //adding
        component.statModifiers().forEach((statistic, statModifiers) -> {
            StatModifiers mainModifiers = statistics.getOrDefault(statistic, new StatModifiers());
            mainModifiers.addModifiers(statModifiers);
        });
    }

    private void removeOldStatisticComponent(StatComponent oldComponent) {
        Map<Statistic, List<StatModifier>> modifiers = oldComponent.statModifiers();
        StatisticsComponent statComponent = getComponent(StatisticsComponent.class);
        Map<Statistic, StatModifiers> statistics = statComponent.statistics();

        //removing old ones
        modifiers.forEach((statistic, statModifiers) -> {
            StatModifiers mainModifiers = statistics.get(statistic);
            mainModifiers.removeModifiers(statModifiers);
        });
        //removed their stats
        removeComponent(oldComponent); //wipe them from here.
    }

    public void updateStatisticComponent(StatComponent newComponent) {
        if (this.hasComponent(newComponent.getClass())) {
            StatComponent oldComponent = getComponent(newComponent.getClass());
            removeOldStatisticComponent(oldComponent);
            addStatisticComponent(newComponent);
        }
    }

}
