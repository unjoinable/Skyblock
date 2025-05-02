package net.skyblock.item.enums;

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
            SWORD, AXE
    );

    private static final Set<ItemCategory> REFORGEABLE_CATEGORIES = EnumSet.of(
            HELMET, CHESTPLATE, LEGGINGS, BOOTS, SWORD, BOW, FISHING_ROD, PICKAXE,
            AXE, HOE, DRILL, SHEARS, ACCESSORY, BELT, NECKLACE, BRACELET, CLOAK
    );

    /**
     * Default constructor for enum values that do not require a custom name.
     * <p>
     * This constructor is used by enum constants such as HELMET, CHESTPLATE
     * and others that do not provide a custom name and rely on the default enum name (e.g., "HELMET" or "SWORD").
     */
    ItemCategory() {
        this.name = name().replace("_", "");
    }

    /**
     * Constructor for enum values that require a custom display name.
     * <p>
     * This constructor allows the enum constant to have a custom name, which is passed as an argument.
     * For example, this could be used for item categories like NONE or others that require a custom string value.
     *
     * @param name The custom name to set for the enum constant.
     */
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

    /**
     * @return True if this is reforgable
     */
    public boolean isReforgeable() {
        return REFORGEABLE_CATEGORIES.contains(this);
    }
}
