package com.github.unjoinable.skyblock.user.data;

import com.github.unjoinable.skyblock.skill.Skill;
import com.github.unjoinable.skyblock.user.SkyblockPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.Map;

public class PlayerData {
    private long coins;
    private long bits;
    private Map<Skill, Long> skills;
    
    /**
     * Populates the PlayerData object with data from a Skyblock Player.
     * This method extracts coins, bits, skill experience and a lot more from the given player
     * and stores them in the current PlayerData instance.
     *
     * @param player The Skyblock Player object from which to extract data.
     * @return The current PlayerData instance, populated with the player's data.
     */
    public @NotNull PlayerData fromPlayer(@NotNull SkyblockPlayer player) {
        this.coins = player.getCoins();
        this.bits = player.getBits();
        this.skills = new EnumMap<>(Skill.class);
        
        for (Skill skill : Skill.values()) {
            this.skills.put(skill, player.getSkillXP(skill));
        }
        
        return this;
    }
}
