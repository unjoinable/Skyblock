package io.github.unjoinable.skyblock.user.profile;

import io.github.unjoinable.skyblock.enums.GamemodeType;
import io.github.unjoinable.skyblock.enums.ProfileType;
import io.github.unjoinable.skyblock.user.profile.data.PlayerData;

import java.util.Map;
import java.util.UUID;

public record Profile(ProfileType profileType,
                      GamemodeType gamemodeType,
                      String prettyName,
                      Map<UUID, PlayerData> playerData) {

    private static final String[] PRETTY_NAMES =  {
            "Zucchini", "Papaya", "Watermelon", "Pineapple", "Lemon", "Apple", "Banana", "Orange", "Pear", "Coconuts",
            "Cherry", "Strawberry", "Raspberry","Kiwi", "Mango", "Pomegranate", "Grape", "Avocado", "Tomato", "Cucumber",
            "Carrot", "Potato", "Onion", "Garlic", "Celery", "Broccoli", "Cauliflower", "Spinach", "Asparagus"};

    public static Profile createProfile(ProfileType profileType, GamemodeType gamemodeType) {

    }


}
