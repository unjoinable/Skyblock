package net.skyblock.item.handlers;

import net.minestom.server.component.DataComponents;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.component.HeadProfile;
import net.skyblock.item.components.HeadTextureComponent;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class HeadTextureHandler implements StackWriterHandler<HeadTextureComponent> {
    private static final String ID = "skin";

    /**
     * Writes component-specific data to the provided ItemStack builder.
     * <p>
     * Implementations should modify the builder to include all relevant data
     * from the component this handler is responsible for.
     *
     * @param component the component to write data from
     * @param builder   the ItemStack builder to write component data to
     */
    @Override
    public void write(@NotNull HeadTextureComponent component, ItemStack.@NotNull Builder builder) {
        PlayerSkin skin = new PlayerSkin(component.texture(), UUID.randomUUID().toString());
        HeadProfile profile = new HeadProfile(skin);
        builder.set(DataComponents.PROFILE, profile);
    }

    /**
     * Returns the component type this handler is responsible for
     */
    @Override
    public @NotNull Class<HeadTextureComponent> componentType() {
        return HeadTextureComponent.class;
    }

    /**
     * Returns the unique identifier for this component type
     */
    @Override
    public @NotNull String componentId() {
        return ID;
    }
}
