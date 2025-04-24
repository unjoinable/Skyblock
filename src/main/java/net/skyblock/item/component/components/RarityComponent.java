package net.skyblock.item.component.components;

import net.skyblock.item.component.ComponentContainer;
import net.skyblock.item.component.trait.DeserializableComponent;
import net.skyblock.item.component.trait.LoreComponent;
import net.skyblock.item.component.trait.SerializableComponent;
import net.skyblock.item.enums.ItemCategory;
import net.skyblock.item.enums.Rarity;
import net.skyblock.item.service.ComponentResolver;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.item.ItemStack;
import net.minestom.server.tag.Tag;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * A class representing the rarity component for an item, which
 * includes the rarity level and whether the rarity has been upgraded.
 */
public final class RarityComponent implements LoreComponent, SerializableComponent, DeserializableComponent {
    private static final Tag<String> RARITY = Tag.String("rarity");
    private static final Tag<Boolean> IS_UPGRADED = Tag.Boolean("is_upgraded");
    private final Rarity rarity;
    private final boolean isUpgraded;
    private final ComponentResolver componentResolver;

    /**
     * Constructs a RarityComponent with the specified rarity, upgrade status, and category resolver.
     * @param rarity The rarity level of the item.
     * @param isUpgraded A boolean indicating if the rarity has been upgraded.
     * @param componentResolver The resolver for item categories.
     */
    public RarityComponent(@NotNull Rarity rarity, boolean isUpgraded, @NotNull ComponentResolver componentResolver) {
        this.rarity = rarity;
        this.isUpgraded = isUpgraded;
        this.componentResolver = componentResolver;
    }

    /**
     * Constructs a RarityComponent with the specified rarity and upgrade status.
     * @param rarity The rarity level of the item.
     * @param isUpgraded A boolean indicating if the rarity has been upgraded.
     */
    public RarityComponent(@NotNull Rarity rarity, boolean isUpgraded) {
        this(rarity, isUpgraded, new ComponentResolver());
    }

    /**
     * Constructs a RarityComponent with the specified rarity and upgrade status.
     * @param rarity The rarity level of the item.
     */
    public RarityComponent(@NotNull Rarity rarity) {
        this(rarity, false);
    }


    /**
     * Retrieves the rarity associated with this component.
     * @return The {@link Rarity} of the item.
     */
    public @NotNull Rarity getRarity() {
        return rarity;
    }

    /**
     * Indicates whether the rarity is upgraded.
     * @return A boolean indicating if the rarity is upgraded.
     */
    public boolean isUpgraded() {
        return isUpgraded;
    }

    @Override
    public int lorePriority() {
        return 100;
    }


    @Override
    public @NotNull List<Component> generateLore(@NotNull ComponentContainer container) {
        Rarity rarity = isUpgraded ? this.rarity.upgrade() : this.rarity;
        ItemCategory category = componentResolver.resolveCategory(container);
        TextColor color = rarity.getColor();

        Component base = Component.text(rarity.name() + " " + category.getName())
                .color(color)
                .decorate(TextDecoration.BOLD)
                .decoration(TextDecoration.ITALIC, false);

        Component result = isUpgraded
                ? Component.empty()
                .append(Component.text("a ", color).decorate(TextDecoration.OBFUSCATED).decoration(TextDecoration.ITALIC, false))
                .append(base)
                .append(Component.text(" a", color).decorate(TextDecoration.OBFUSCATED).decoration(TextDecoration.ITALIC, false))
                : base;

        return Collections.singletonList(result);
    }

    @Override
    public void write(ItemStack.@NotNull Builder builder) {
        builder.set(RARITY, rarity.name())
               .set(IS_UPGRADED, isUpgraded);
    }

    public static @NotNull Optional<? extends DeserializableComponent> read(@NotNull ItemStack itemStack) {
        if (itemStack.hasTag(RARITY) && itemStack.hasTag(IS_UPGRADED)) {
            return Optional.of(new RarityComponent
                    (Rarity.getRarity(itemStack.getTag(RARITY)),
                            itemStack.getTag(IS_UPGRADED)));
        }
        return Optional.empty();
    }
}
