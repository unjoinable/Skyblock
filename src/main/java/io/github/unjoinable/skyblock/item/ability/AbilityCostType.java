package io.github.unjoinable.skyblock.item.ability;

public enum AbilityCostType {
    MANA,
    HEALTH,
    COINS,
    ;

    @Override
    public String toString() {
        String[] words = name().toLowerCase().split("_");
        StringBuilder titleCase = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                titleCase.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1)).append(" ");
            }
        }
        return titleCase.toString().trim();
    }
}
