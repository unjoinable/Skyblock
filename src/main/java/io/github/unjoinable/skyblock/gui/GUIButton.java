package io.github.unjoinable.skyblock.gui;

import io.github.unjoinable.skyblock.util.NamespacedId;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

public class GUIButton {

    public static final Button CLOSE_BUTTON = new Button(
            new NamespacedId("common", "close"),
            (_) -> ItemStack.of(Material.BARRIER),
            null
    );

}
