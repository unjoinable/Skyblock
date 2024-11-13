package io.github.unjoinable.skyblock.user;

import io.github.unjoinable.skyblock.item.ability.Ability;
import io.github.unjoinable.skyblock.util.NamespacedId;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class AbilityHandler {
    private final SkyblockPlayer player;
    private final Map<NamespacedId, Long> cooldowns;

    public AbilityHandler(@NotNull SkyblockPlayer player) {
        this.player = player;
        this.cooldowns = new HashMap<>();
    }

    public boolean canUseAbility(@NotNull Ability ability) {
        NamespacedId id = ability.id();
        return !cooldowns.containsKey(id) || getRemainingCooldown(ability) == 0;
    }

    public long getRemainingCooldown(@NotNull Ability ability) {
        long lastUse = cooldowns.get(ability.id());
        long cooldown = ability.cooldownInMs();
        long current = System.currentTimeMillis();

        long remaining = (lastUse + cooldown) - current;

        return remaining < 0 ? 0 : remaining;
    }

    public void startCooldown(@NotNull Ability ability) {
        if (ability.cooldownInMs() == 0) return;
        cooldowns.put(ability.id(), System.currentTimeMillis());
    }
}
