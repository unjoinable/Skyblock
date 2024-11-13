package io.github.unjoinable.skyblock.gui;

import io.github.unjoinable.skyblock.user.SkyblockPlayer;
import io.github.unjoinable.skyblock.util.NamespacedId;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

public interface ClickableItem {

    @NotNull NamespacedId id();

    @NotNull BiConsumer<SkyblockPlayer, InventoryGUI> onClick();

}
