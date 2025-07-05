package net.unjoinable.skyblock.item.attribute.impls;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.minestom.server.codec.Codec;
import net.minestom.server.codec.StructCodec;
import net.unjoinable.skyblock.item.ItemMetadata;
import net.unjoinable.skyblock.item.ability.ItemAbility;
import net.unjoinable.skyblock.item.ability.traits.SilentAbility;
import net.unjoinable.skyblock.item.attribute.AttributeContainer;
import net.unjoinable.skyblock.item.attribute.traits.ItemAttribute;
import net.unjoinable.skyblock.item.attribute.traits.LoreAttribute;
import net.unjoinable.skyblock.player.SkyblockPlayer;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;
import static net.kyori.adventure.text.format.TextDecoration.BOLD;
import static net.kyori.adventure.text.format.TextDecoration.ITALIC;

/**
 * An attribute that adds one or more abilities to an item.
 * Each ability contributes descriptive lore and functional behavior.
 */
public record AbilityAttribute(List<ItemAbility> abilities) implements LoreAttribute {
    public static final Key KEY = Key.key("attribute:ability");
    public static final Codec<AbilityAttribute> CODEC = StructCodec.struct(
            "abilities", ItemAbility.CODEC.list(), AbilityAttribute::abilities,
            AbilityAttribute::new
    );

    @Override
    public @NotNull Key key() {
        return KEY;
    }

    @Override
    public Codec<? extends ItemAttribute> codec() {
        return CODEC;
    }

    /**
     * Generates lore lines for each ability in this attribute.
     *
     * @param player    the player viewing the item, may be null
     * @param container the container holding attributes
     * @param metadata  the item metadata
     * @return a list of formatted lore components
     */
    @Override
    public List<Component> loreLines(@Nullable SkyblockPlayer player, AttributeContainer container, ItemMetadata metadata) {
        List<Component> lore = new ArrayList<>();

        for (int i = 0; i < abilities.size(); i++) {
            ItemAbility ability = abilities.get(i);
            if (ability instanceof SilentAbility) continue;
            lore.addAll(formatAbility(ability));

            if (i < abilities.size() - 1) {
                lore.add(Component.empty());
            }
        }

        return lore;
    }

    /**
     * Determines where this attribute's lore appears relative to others.
     *
     * @return the priority value (lower means earlier in display)
     */
    @Override
    public int priority() {
        return 100;
    }

    /**
     * Formats the display of a single ability, including its name, trigger, description, and cost.
     *
     * @param ability the item ability to format
     * @return a list of components representing the formatted lore
     */
    private List<Component> formatAbility(ItemAbility ability) {
        List<Component> components = new ArrayList<>();

        components.add(
                text("Ability: ", GOLD).decoration(ITALIC, false)
                        .append(text(ability.displayName(), GOLD).decoration(ITALIC, false))
                        .append(text(" " + ability.trigger().displayName(), YELLOW, BOLD))
        );

        components.addAll(ability.description());

        if (ability.cost() > 0) {
            components.add(
                    text("Mana cost: ", DARK_GRAY).decoration(ITALIC, false)
                            .append(text(ability.cost(), DARK_AQUA))
            );
        }

        return components;
    }
}