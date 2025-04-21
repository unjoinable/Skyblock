package com.github.unjoinable.skyblock.item.component.components;

import com.github.unjoinable.skyblock.item.component.ComponentContainer;
import com.github.unjoinable.skyblock.item.component.trait.DeserializableComponent;
import com.github.unjoinable.skyblock.item.component.trait.SerializableComponent;
import com.github.unjoinable.skyblock.item.component.trait.StatModifierComponent;
import com.github.unjoinable.skyblock.item.enums.ModifierType;
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
 * Represents the "Art of Peace" item component (+40 Health).
 * Serializable to NBT ({@code art_of_power} tag), but not deserialized from NBT by this class.
 * Acts as a Hot Potato type modifier with red text.
 */
public class ArtOfPeaceComponent implements StatModifierComponent, SerializableComponent, DeserializableComponent {
    private static final Tag<Boolean> ART_OF_POWER = Tag.Boolean("art_of_power").defaultValue(false);
    private final boolean isApplied;

    public ArtOfPeaceComponent(boolean isApplied) {
        this.isApplied = isApplied;
    }

    public ArtOfPeaceComponent() {
        this(false);
    }

    @Override
    public @NotNull Optional<? extends DeserializableComponent> fromNBT(@NotNull ItemStack itemStack) {
        boolean storedIsApplied = itemStack.getTag(ART_OF_POWER);
        return Optional.of(new ArtOfPeaceComponent(storedIsApplied));
    }


    @Override
    public void nbtWriter(ItemStack.@NotNull Builder builder) {
        builder.set(ART_OF_POWER, isApplied);
    }

    @Override
    public @NotNull ModifierType getModifierType() {
        return ModifierType.HOT_POTATO;
    }

    /**
     * Provides the stat modification: +40 base Health.
     */
    @Override
    public @NotNull StatProfile getStatProfile(ComponentContainer container) {
        StatProfile statProfile = new StatProfile();
        statProfile.addStat(Statistic.HEALTH, StatValueType.BASE, 40);
        return statProfile;
    }

    @Override
    public TextColor getModifierColor() {
        return NamedTextColor.RED;
    }
}