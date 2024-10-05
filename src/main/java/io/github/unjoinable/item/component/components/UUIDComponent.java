package io.github.unjoinable.item.component.components;

import com.google.common.collect.ImmutableSet;
import io.github.unjoinable.enums.ItemCategory;
import io.github.unjoinable.item.SkyblockItem;
import io.github.unjoinable.item.component.ItemComponent;
import io.github.unjoinable.item.component.Retrievable;
import net.minestom.server.item.ItemStack;
import net.minestom.server.tag.Tag;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.UUID;

import static io.github.unjoinable.enums.ItemCategory.*;

public record UUIDComponent(UUID uuid) implements ItemComponent, Retrievable {
    private static final Tag<UUID> UUID_TAG = Tag.UUID("uuid");
    private static final Set<ItemCategory> UUID_CATEGORY = ImmutableSet.of(
            SWORD, LONGSWORD, HELMET, CHESTPLATE, LEGGINGS, AXE, DEPLOYABLE, HOE, NECKLACE, NONE, PICKAXE, BELT, BOW);

    @Override
    public @NotNull Class<? extends ItemComponent> type() {
        return UUIDComponent.class;
    }

    @Override
    public void apply(@NotNull SkyblockItem item, ItemStack.@NotNull Builder builder) {
        builder.set(UUID_TAG, uuid);
    }

    @Override
    public @NotNull ItemComponent defaultComponent() {
        return new UUIDComponent(UUID.randomUUID());
    }

    public static boolean canHasUUID(ItemCategory category) {
        return UUID_CATEGORY.contains(category);
    }

    @Override
    public ItemComponent retrieve(@NotNull ItemStack item) {
        if (item.hasTag(UUID_TAG)) {
            return new UUIDComponent(item.getTag(UUID_TAG));
        }
        return null;
    }
}
