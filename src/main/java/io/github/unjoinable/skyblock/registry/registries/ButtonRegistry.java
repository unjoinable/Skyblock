package io.github.unjoinable.skyblock.registry.registries;

import io.github.unjoinable.skyblock.gui.Button;
import io.github.unjoinable.skyblock.registry.Registry;
import io.github.unjoinable.skyblock.util.NamespacedId;

public class ButtonRegistry extends Registry<NamespacedId, Button> {
    private static final ButtonRegistry INSTANCE = new ButtonRegistry();

    public static ButtonRegistry getInstance() {
        return INSTANCE;
    }
}
