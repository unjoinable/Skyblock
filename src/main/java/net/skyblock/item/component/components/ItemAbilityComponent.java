package net.skyblock.item.component.components;

import net.skyblock.item.ability.Ability;
import net.skyblock.item.component.ItemComponent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Component for items that have abilities.
 * Holds and manages the abilities an item can use.
 */
public class ItemAbilityComponent implements ItemComponent {
    private final List<Ability> abilities;

    public ItemAbilityComponent(List<Ability> abilities) {
        this.abilities = new ArrayList<>(abilities);
    }

    /**
     * Get all abilities on this item
     * @return unmodifiable list of abilities
     */
    public List<Ability> getAbilities() {
        return Collections.unmodifiableList(abilities);
    }

    /**
     * Find an ability by its identifier
     * @param id the ability identifier
     * @return the ability or null if not found
     */
    public Ability getAbility(String id) {
        return abilities.stream()
                .filter(ability -> ability.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Class<? extends ItemComponent> getType() {
        return ItemAbilityComponent.class;
    }
}