package net.skyblock.item.component.handlers;

import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.text.Component;
import net.skyblock.item.component.ComponentContainer;
import net.skyblock.item.component.impl.HotPotatoBookComponent;
import net.skyblock.item.enums.ItemCategory;
import net.skyblock.item.component.trait.NBTHandler;
import net.skyblock.item.component.trait.ModifierHandler;
import net.skyblock.item.component.service.ComponentResolver;
import net.skyblock.stats.StatProfile;
import net.skyblock.stats.StatValueType;
import net.skyblock.stats.Statistic;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import static net.kyori.adventure.text.format.NamedTextColor.YELLOW;
import static net.skyblock.utils.Utils.formatStatValue;

public class HotPotatoBookHandler implements NBTHandler<HotPotatoBookComponent>, ModifierHandler<HotPotatoBookComponent> {
    private static final String ID = "hot_potato_book";
    private static final String KEY_COUNT = "count";

    /**
     * Deserializes an ItemComponent from NBT data.
     *
     * @param nbt The NBT data containing component information
     * @return A new component instance created from the NBT data
     */
    @Override
    public @NotNull Optional<HotPotatoBookComponent> fromNbt(CompoundBinaryTag nbt) {
        int count = nbt.getInt(KEY_COUNT);

        if (count == 0) {
            return Optional.empty();
        }
        return Optional.of(new HotPotatoBookComponent(count));
    }

    /**
     * Serializes an ItemComponent to NBT data.
     *
     * @param component The component to serialize
     * @return The NBT representation of the component
     */
    @Override
    public CompoundBinaryTag toNbt(@NotNull HotPotatoBookComponent component) {
        CompoundBinaryTag.Builder builder = CompoundBinaryTag.builder();
        builder.putInt(KEY_COUNT, component.count());

        return builder.build();
    }

    /**
     * Returns the component type this handler is responsible for
     */
    @Override
    public @NotNull Class<HotPotatoBookComponent> componentType() {
        return HotPotatoBookComponent.class;
    }

    /**
     * Returns the unique identifier for this component type
     */
    @Override
    public @NotNull String componentId() {
        return ID;
    }

    /**
     * Gets the stat profile containing all statistic modifications provided by this component.
     *
     * @param component The component to get data from
     * @param container The container holding the item component
     * @return A stat profile with all applicable statistic modifications
     */
    @Override
    public @NotNull StatProfile getStatProfile(@NotNull HotPotatoBookComponent component, @NotNull ComponentContainer container) {
        StatProfile statProfile = new StatProfile();
        ComponentResolver resolver = new ComponentResolver();
        ItemCategory itemCategory = resolver.resolveCategory(container);
        int count = component.count();

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

    /**
     * Formats a statistic and its value into a displayable text component.
     * Used for rendering the stat in tooltips, GUIs, or other displays.
     *
     * @param stat  The statistic to format
     * @param value The value of the statistic
     * @return A formatted text component for display
     */
    @Override
    public @NotNull Component formatStatDisplay(@NotNull Statistic stat, double value) {
        return Component.text("(" + formatStatValue(value, stat) + ")", YELLOW);
    }
}
