package io.github.unjoinable.skyblock.user.profile.data;

import io.github.unjoinable.skyblock.enums.Skill;

import java.util.EnumMap;
import java.util.Map;

public class PlayerData {
    private long coins = 0;
    private long bits = 0;
    private Map<Skill, Long> skills = new EnumMap<>(Skill.class);

    public PlayerData() { //default constructor for gson.

    }

    //giving
    public void addCoins(long coins) {
        this.coins += coins;
    }

    public void addBits(long bits) {
        this.bits += bits;
    }

    public void addSkillXp(Skill skill, long xp) {
        this.skills.put(skill, this.skills.getOrDefault(skill, 0L) + xp);
    }

    //taking
    public void removeCoins(long coins) {
        this.coins -= coins;
    }

    public void removeBits(long bits) {
        this.bits -= bits;
    }

}
