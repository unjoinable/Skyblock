package io.github.unjoinable.skyblock.enums;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

    ItemCategory() {
        name = name().replaceAll("_", "");
    }

    ItemCategory(String name) {
        this.name = name;
    }

    public @NotNull String getName() {
        return name;
    }

    public static ItemCategory getItemCategory(@Nullable String str) {
        if (str == null) return ItemCategory.NONE;
        try {
            return ItemCategory.valueOf(str.toUpperCase());
        } catch (IllegalArgumentException ex) {
            return ItemCategory.NONE;
        }
    }
}
