package com.github.unjoinable.skyblock.item;

import com.google.common.collect.ImmutableSet;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public enum ItemCategory {
    NONE(""),
    HELMET,
    CHESTPLATE,
    LEGGINGS,
    BOOTS,
    SWORD,
    PICKAXE,
    PET_ITEM,
    COSMETIC,
    REFORGE_STONE,
    BELT,
    NECKLACE,
    GLOVES,
    ACCESSORY,
    CLOAK,
    AXE,
    HOE,
    MEMENTO,
    FISHING_ROD,
    FISHING_WEAPON,
    BAIT,
    BRACELET,
    WAND,
    PORTAL,
    BOW,
    DUNGEON_PASS,
    ARROW,
    SPADE,
    CONSUMABLE,
    CARNIVAL_MASK,
    DEPLOYABLE,
    DRILL,
    SHEARS,
    LONGSWORD,
    GAUNTLET,
    TRAVEL_SCROLL,
    ARROW_POISON,
    CHISEL,
    VACUUM;

    private final String name;
    private static final Set<ItemCategory> ARMOR_CATEGORIES = ImmutableSet.of(
            ItemCategory.HELMET,
            ItemCategory.CHESTPLATE,
            ItemCategory.LEGGINGS,
            ItemCategory.BOOTS
    );

    ItemCategory() {
        name = name().replaceAll("_", "");
    }

    ItemCategory(String name) {
        this.name = name;
    }

    public @NotNull String getName() {
        return name;
    }

    public boolean isArmor() {
        return ARMOR_CATEGORIES.contains(this);
    }
}
