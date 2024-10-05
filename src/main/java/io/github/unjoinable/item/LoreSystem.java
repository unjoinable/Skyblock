package io.github.unjoinable.item;

import io.github.unjoinable.item.component.ItemComponent;
import io.github.unjoinable.item.component.Loreable;
import io.github.unjoinable.user.SkyblockPlayer;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

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
        Map<Class<? extends ItemComponent>, ItemComponent> components = item.components();

        components.values().stream().filter(Loreable.class::isInstance)
                .sorted(Comparator.comparingInt(component -> ((Loreable) component).priority()).reversed())
                .forEach(component -> {
                    List<Component> componentLore = ((Loreable) component).lore(item);
                    if (!componentLore.isEmpty()) {
                        if (!lore.isEmpty()) lore.add(Component.empty());
                        lore.addAll(componentLore);
                    }
                });
        return lore;
    }
}
