package com.github.unjoinable.skyblock.item.component.components;

import com.github.unjoinable.skyblock.item.component.ComponentContainer;
import com.github.unjoinable.skyblock.item.component.trait.DeserializableComponent;
import com.github.unjoinable.skyblock.item.component.trait.SerializableComponent;
import com.github.unjoinable.skyblock.item.component.trait.StatModifierComponent;
import com.github.unjoinable.skyblock.item.enums.ItemCategory;
import com.github.unjoinable.skyblock.item.enums.ModifierType;
import com.github.unjoinable.skyblock.item.service.ComponentResolver;
import com.github.unjoinable.skyblock.stats.StatProfile;
import com.github.unjoinable.skyblock.stats.StatValueType;
import com.github.unjoinable.skyblock.stats.Statistic;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.minestom.server.item.ItemStack;
import net.minestom.server.tag.Tag;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Represents a Hot Potato Book component that can be applied to items.
 * In Hypixel SkyBlock, Hot Potato Books add +2 to various stats when applied to an item.
 * Up to 10 Hot Potato Books can be applied to an item, with each application
 * providing additional stats.
 * <p></p>
 * Different item categories receive different stat bonuses:
 * - Weapons (SWORD, AXE): +2 Damage and +2 Strength per book
 * - Armor (HELMET, CHESTPLATE, LEGGINGS, BOOTS): +2 Defense and +4 Health per book
 * - Other items cannot have Hot Potato Books applied
 */
public class HotPotatoBookComponent implements StatModifierComponent, SerializableComponent, DeserializableComponent {
    private static final Tag<Integer> HOT_POTATO_BOOKS = Tag.Integer("hot_potato_books");
    private static final int MAX_HOT_POTATO_BOOKS = 10;
    private final int count;

    /**
     * Creates a new Hot Potato Book component with the specified count.
     *
     * @param count The number of Hot Potato Books applied (0-10)
     */
    public HotPotatoBookComponent(int count) {
        this.count = Math.min(Math.max(0, count), MAX_HOT_POTATO_BOOKS);
    }

    /**
     * Creates a new Hot Potato Book component with no books applied.
     */
    public HotPotatoBookComponent() {
        this(0);
    }

    /**
     * Gets the number of Hot Potato Books applied.
     *
     * @return The number of applied books
     */
    public int getCount() {
        return count;
    }

    /**
     * Checks if this component has the maximum number of Hot Potato Books applied.
     *
     * @return True if 10 books are applied, false otherwise
     */
    public boolean isMaxed() {
        return count >= MAX_HOT_POTATO_BOOKS;
    }

    /**
     * Creates a new component with one more Hot Potato Book applied.
     *
     * @return A new component with incremented count, or this component if already maxed
     */
    public HotPotatoBookComponent incrementCount() {
        if (isMaxed()) {
            return this;
        }
        return new HotPotatoBookComponent(count + 1);
    }


    @Override
    public @NotNull ModifierType getModifierType() {
        return ModifierType.HOT_POTATO;
    }

    @Override
    public @NotNull StatProfile getStatProfile(ComponentContainer container) {
        StatProfile statProfile = new StatProfile();
        ComponentResolver resolver = new ComponentResolver();
        ItemCategory itemCategory = resolver.resolveCategory(container);

        if (itemCategory.isWeapon()) {
            // Weapon stats - Damage and Strength
            statProfile.addStat(Statistic.DAMAGE, StatValueType.BASE, 2 * count);
            statProfile.addStat(Statistic.STRENGTH, StatValueType.BASE, 2 * count);
        } else if (itemCategory.isArmor()) {
            // Armor stats - Defense and Health
            statProfile.addStat(Statistic.DEFENSE, StatValueType.BASE, 2 * count);
            statProfile.addStat(Statistic.HEALTH, StatValueType.BASE, 4 * count);
        }

        return statProfile;
    }

    @Override
    public TextColor getModifierColor() {
        return NamedTextColor.YELLOW;
    }

    @Override
    public char getOpenBracket() {
        return '(';
    }

    @Override
    public char getCloseBracket() {
        return ')';
    }

    @Override
    public void write(ItemStack.@NotNull Builder builder) {
        builder.setTag(HOT_POTATO_BOOKS, count);
    }

    public static @NotNull Optional<? extends DeserializableComponent> read(@NotNull ItemStack itemStack) {
        if (itemStack.hasTag(HOT_POTATO_BOOKS)) {
            return Optional.of(new HotPotatoBookComponent(itemStack.getTag(HOT_POTATO_BOOKS)));
        }
        return Optional.empty();
    }
}