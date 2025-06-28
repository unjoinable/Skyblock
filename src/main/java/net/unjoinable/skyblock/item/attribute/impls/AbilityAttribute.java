package net.unjoinable.skyblock.item.attribute.impls;

import net.kyori.adventure.text.Component;
import net.minestom.server.codec.Codec;
import net.minestom.server.codec.StructCodec;
import net.unjoinable.skyblock.item.ItemMetadata;
import net.unjoinable.skyblock.item.ability.ItemAbility;
import net.unjoinable.skyblock.item.attribute.AttributeContainer;
import net.unjoinable.skyblock.item.attribute.traits.ItemAttribute;
import net.unjoinable.skyblock.item.attribute.traits.LoreAttribute;
import net.unjoinable.skyblock.player.SkyblockPlayer;
import net.unjoinable.skyblock.utils.NamespaceId;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;

public record AbilityAttribute(List<ItemAbility> abilities) implements LoreAttribute {
    public static final NamespaceId ID = NamespaceId.fromString("attribute:ability");
    public static final Codec<AbilityAttribute> CODEC = StructCodec.struct(
            "abilities", ItemAbility.CODEC.list(), AbilityAttribute::abilities,
            AbilityAttribute::new
    );

    @Override
    public NamespaceId id() {
        return ID;
    }

    @Override
    public Codec<? extends ItemAttribute> codec() {
        return CODEC;
    }

    @Override
    public List<Component> loreLines(@Nullable SkyblockPlayer player, AttributeContainer container, ItemMetadata metadata) {
        List<Component> lore = new ArrayList<>();
        abilities.forEach(ability -> {
            lore.addAll(formatAbility(ability));
            lore.add(Component.empty());
        });

        return lore;
    }

    @Override
    public int priority() {
        return 100;
    }

    private List<Component> formatAbility(ItemAbility ability) {
        List<Component> components = new ArrayList<>();
        components.add(text("Ability: " + ability.id(), GOLD).append(text(" " + ability.trigger().name(), YELLOW)));
        components.addAll(ability.description());
        components.add(text("Mana cost: ", DARK_GRAY).append(text(ability.cost(), DARK_AQUA)));
        return components;
    }
}
