package net.unjoinable.skyblock.combat.damage;

import net.minestom.server.MinecraftServer;
import net.minestom.server.registry.RegistryKey;

import static net.minestom.server.entity.damage.DamageType.*;

/**
 * Represents the different types of damage that can be dealt in the Skyblock combat system.
 * Each damage type has unique properties that affect how damage is calculated and applied.
 */
public enum DamageType {
    MELEE_PLAYER(PLAYER_ATTACK, false),
    MELEE_ENTITY(MOB_ATTACK, false),
    RANGED(ARROW, false),
    MAGIC_DAMAGE(MAGIC, false),
    FIREBALL_DAMAGE(FIREBALL, false),
    FIRE(IN_FIRE, false),
    EXPLOSION_DAMAGE(EXPLOSION, false),

    VOID(OUT_OF_WORLD, true),
    WITHER_DAMAGE(WITHER, true),
    WITHER_SKULL_DAMAGE(WITHER_SKULL, true),
    LIGHTNING(LIGHTNING_BOLT, true),
    UNKNOWN(GENERIC, true);

    private final int typeId;
    private final boolean bypassesDefense;

    DamageType(RegistryKey<net.minestom.server.entity.damage.DamageType> damageType, boolean bypassesDefense) {
        this.typeId = MinecraftServer.getDamageTypeRegistry().getId(damageType);
        this.bypassesDefense = bypassesDefense;
    }

    /**
     * Gets the unique identifier for this damage type from the Minecraft registry.
     *
     * @return the type ID
     */
    public int typeId() {
        return typeId;
    }

    /**
     * Checks if this damage type bypasses conventional defenses like armor and resistance effects.
     *
     * @return true if this damage bypasses defenses, false otherwise
     */
    public boolean bypassesDefense() {
        return bypassesDefense;
    }
}