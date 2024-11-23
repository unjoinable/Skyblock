package com.github.unjoinable.skyblock.item.component.components;

import com.github.unjoinable.skyblock.item.SkyblockItem;
import com.github.unjoinable.skyblock.item.component.BasicComponent;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.component.HeadProfile;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record HelmetSkinComponent(String texture) implements BasicComponent {

    @Override
    public void applyEffect(@NotNull SkyblockItem item, ItemStack.@NotNull Builder builder) {
        if (item.container().getComponent(MaterialComponent.class).material() == Material.PLAYER_HEAD) {
            PlayerSkin skin = new PlayerSkin(texture, UUID.randomUUID().toString());
            HeadProfile profile = new HeadProfile(skin);
            builder.set(net.minestom.server.item.ItemComponent.PROFILE, profile);
        }
    }
}
