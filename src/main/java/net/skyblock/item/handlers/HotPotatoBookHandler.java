package net.skyblock.item.handlers;

import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.skyblock.item.components.HotPotatoBookComponent;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class HotPotatoBookHandler implements NBTHandler<HotPotatoBookComponent> {
    private static final String ID = "skyblock:hot_potato_book_handler";
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
}
