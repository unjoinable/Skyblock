package io.github.unjoinable.item.component.components;

import io.github.unjoinable.item.SkyblockItem;
import io.github.unjoinable.item.component.ItemComponent;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.component.HeadProfile;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record ArmorSkinComponent(String texture) implements ItemComponent {
    private static final ArmorSkinComponent DEFAULT = new ArmorSkinComponent(null);

    @Override
    public @NotNull Class<? extends ItemComponent> type() {
        return ArmorSkinComponent.class;
    }

    @Override
    public void apply(@NotNull SkyblockItem item, ItemStack.@NotNull Builder builder) {
        if (item.getOrDefault(MaterialComponent.class).material() == Material.PLAYER_HEAD) {
            PlayerSkin skin = new PlayerSkin(texture, UUID.randomUUID().toString());
            HeadProfile profile = new HeadProfile(skin);
            builder.set(net.minestom.server.item.ItemComponent.PROFILE, profile);
        }
    }

    @Override
    public @NotNull ItemComponent defaultComponent() {
        return DEFAULT;
    }
}
