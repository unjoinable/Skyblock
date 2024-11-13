package io.github.unjoinable.skyblock.item.ability;

/**
 * Represents the different types of cost associated with abilities in the game.
 * Each enum constant represents a specific cost type.
 */
public enum AbilityCostType {
    MANA,
    HEALTH,
    COINS,
    ;

    /**
     * Converts the enum constant to a human-readable string.
     *
     * @return A string representation of the enum constant in title case,
     *         with underscores replaced by spaces.
     */
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
