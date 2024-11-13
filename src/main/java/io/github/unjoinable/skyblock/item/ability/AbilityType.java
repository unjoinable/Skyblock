package io.github.unjoinable.skyblock.item.ability;

public enum AbilityType {
    RIGHT_CLICK,
    LEFT_CLICK,
    PASSIVE,
    SNEAK,
    FULL_SET_BONUS,
    ;

    @Override
    public String toString() {
        return super.toString().replace("_", " ");
    }
}
