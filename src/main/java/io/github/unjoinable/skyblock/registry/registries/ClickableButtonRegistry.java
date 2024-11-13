package io.github.unjoinable.skyblock.registry.registries;

import io.github.unjoinable.skyblock.gui.ClickableItem;
import io.github.unjoinable.skyblock.registry.Registry;
import io.github.unjoinable.skyblock.util.NamespacedId;
import org.jetbrains.annotations.NotNull;

public class ClickableButtonRegistry extends Registry<NamespacedId, ClickableItem> {
    private static final ClickableButtonRegistry INSTANCE = new ClickableButtonRegistry();

    @Override
    public void registerAll() {
        add(ClickableItem.CLOSE_BUTTON);
    }

    public static ClickableButtonRegistry getInstance() {
        return INSTANCE;
    }

    public void add(@NotNull ClickableItem clickableItem) {
        add(clickableItem.id(), clickableItem);
    }
}
