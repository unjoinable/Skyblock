package io.github.unjoinable.skyblock.item;

import io.github.unjoinable.skyblock.item.component.LoreableComponent;
import io.github.unjoinable.skyblock.user.SkyblockPlayer;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class LoreSystem {
    private final SkyblockItem item;
    private final List<Component> lore;
    private final SkyblockPlayer player;

    public LoreSystem(@NotNull SkyblockItem item, @Nullable SkyblockPlayer player) {
        this.item = item;
        this.player = player;
        this.lore = new ArrayList<>();
    }

    public List<Component> build() {
        item.container().getAllComponents().values().stream().filter(LoreableComponent.class::isInstance)
                .sorted(Comparator.comparingInt(component -> ((LoreableComponent) component).priority()).reversed())
                .forEach(component -> {
                    List<Component> componentLore = ((LoreableComponent) component).lore(item);
                    if (!componentLore.isEmpty()) {
                        if (!lore.isEmpty()) lore.add(Component.empty());
                        lore.addAll(componentLore);
                    }
                });
        return lore;
    }
}
