package com.github.unjoinable.skyblock.item.enums;

import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.Set;

/**
 * Categories used to classify items.
 */
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

    private static final Set<ItemCategory> ARMOR_CATEGORIES = EnumSet.of(
            HELMET, CHESTPLATE, LEGGINGS, BOOTS
    );

    private static final Set<ItemCategory> WEAPON_CATEGORIES = EnumSet.of(
            ItemCategory.SWORD,
            ItemCategory.AXE
    );

    ItemCategory() {
        this.name = name().replace("_", "");
    }

    ItemCategory(String name) {
        this.name = name;
    }

    /**
     * @return Cleaned display name (no underscores, lowercase etc.)
     */
    public @NotNull String getName() {
        return name;
    }

    /**
     * @return True if this is any piece of armor.
     */
    public boolean isArmor() {
        return ARMOR_CATEGORIES.contains(this);
    }

    /**
     * @return True if this is a weapon;
     */
    public boolean isWeapon() {
        return WEAPON_CATEGORIES.contains(this);
    }
}
