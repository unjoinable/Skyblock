package net.skyblock.item.component.handlers;

import com.google.gson.JsonElement;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.skyblock.item.component.ComponentContainer;
import net.skyblock.item.component.impl.RarityComponent;
import net.skyblock.item.enums.Rarity;
import net.skyblock.item.component.trait.LoreHandler;
import net.skyblock.item.component.trait.NBTHandler;
import net.skyblock.item.component.service.ComponentResolver;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.textOfChildren;
import static net.kyori.adventure.text.format.TextDecoration.*;

public class RarityHandler implements LoreHandler<RarityComponent>, NBTHandler<RarityComponent> {
    private static final String ID = "rarity";
    private static final String KEY_RARITY = "rarity";
    private static final String KEY_UPGRADED = "upgraded";

    /**
     * Priority value to determine lore ordering.
     * Lower = appears earlier in the item lore.
     */
    @Override
    public int lorePriority() {
        return 100;
    }

    /**
     * Generates lore lines for this component.
     *
     * @param component the component to generate lore for
     * @param container the full component container, in case this lore depends on other components
     * @return list of components representing lore lines
     */
    @Override
    public @NotNull List<Component> generateLore(@NotNull RarityComponent component, @NotNull ComponentContainer container) {
        final Rarity itemRarity = component.isUpgraded() ? component.rarity().upgrade() : component.rarity();
        final TextColor color = itemRarity.getColor();
        final String categoryName = new ComponentResolver().resolveCategory(container).getName();

        final Component base = text(itemRarity.name() + " " + categoryName, color, BOLD)
                .decoration(ITALIC, false);

        if (component.isUpgraded()) {
            return Collections.singletonList(
                    textOfChildren(text("a ", color, OBFUSCATED), base, text(" a", color, OBFUSCATED))
                            .decoration(ITALIC, false)
            );
        }

        return Collections.singletonList(base);
    }

    /**
     * Deserializes an ItemComponent from NBT data.
     *
     * @param nbt The NBT data containing component information
     * @return A an optional component instance created from the NBT data
     */
    @Override
    public @NotNull Optional<RarityComponent> fromNbt(CompoundBinaryTag nbt) {
        String rarityName = nbt.getString(KEY_RARITY);
        Rarity rarity = Rarity.getRarity(rarityName);
        boolean upgraded = nbt.getBoolean(KEY_UPGRADED);

        return Optional.of(new RarityComponent(rarity, upgraded));
    }

    /**
     * Serializes an ItemComponent to NBT data.
     *
     * @param component The component to serialize
     * @return The NBT representation of the component
     */
    @Override
    public CompoundBinaryTag toNbt(@NotNull RarityComponent component) {
        CompoundBinaryTag.Builder builder = CompoundBinaryTag.builder();
        builder.putString(KEY_RARITY, component.rarity().name());
        builder.putBoolean(KEY_UPGRADED, component.isUpgraded());

        return builder.build();
    }

    /**
     * Returns the component type this handler is responsible for
     */
    @Override
    public @NotNull Class<RarityComponent> componentType() {
        return RarityComponent.class;
    }

    /**
     * Returns the unique identifier for this component type
     */
    @Override
    public @NotNull String componentId() {
        return ID;
    }

    /**
     * Creates a component instance from JSON data
     *
     * @param json The JSON data to parse
     * @return The created component instance
     * @throws UnsupportedOperationException by default unless overridden
     */
    @Override
    public RarityComponent fromJson(@NotNull JsonElement json) {
        String value = json.getAsString();
        Rarity rarity = Rarity.getRarity(value);
        return new RarityComponent(rarity, false);
    }
}
