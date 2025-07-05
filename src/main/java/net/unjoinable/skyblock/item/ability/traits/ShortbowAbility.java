package net.unjoinable.skyblock.item.ability.traits;

public interface ShortbowAbility extends SilentAbility {

    @Override
    default long cooldown() {
        return 500;
    }
}
