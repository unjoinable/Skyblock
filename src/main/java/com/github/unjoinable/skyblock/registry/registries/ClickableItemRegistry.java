package com.github.unjoinable.skyblock.registry.registries;

import com.github.unjoinable.skyblock.gui.ClickableItem;
import com.github.unjoinable.skyblock.registry.Registry;
import com.github.unjoinable.skyblock.util.NamespacedId;
import org.jetbrains.annotations.NotNull;

public class ClickableItemRegistry extends Registry<NamespacedId, ClickableItem> {
    private static final ClickableItemRegistry INSTANCE = new ClickableItemRegistry();

    @Override
    public void registerAll() {
        add(ClickableItem.CLOSE_BUTTON);
    }

    public static ClickableItemRegistry getInstance() {
        return INSTANCE;
    }

    public void add(@NotNull ClickableItem clickableItem) {
        add(clickableItem.id(), clickableItem);
    }
}
