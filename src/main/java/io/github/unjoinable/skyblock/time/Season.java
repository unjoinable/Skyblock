package io.github.unjoinable.skyblock.time;

import org.jetbrains.annotations.NotNull;

public enum Season {
    EARLY_SPRING("Early Spring"),
    SPRING("Spring"),
    LATE_SPRING("Late Spring"),
    EARLY_SUMMER("Early Summer"),
    SUMMER("Summer"),
    LATE_SUMMER("Late Summer"),
    EARLY_AUTUMN("Early Autumn"),
    AUTUMN("Autumn"),
    LATE_AUTUMN("Late Autumn"),
    EARLY_WINTER("Early Winter"),
    WINTER("Winter"),
    LATE_WINTER("Late Winter"),
    ;

    private final String displayName;

    Season(@NotNull String displayName) {
        this.displayName = displayName;
    }

    public @NotNull String getDisplayName() {
        return displayName;
    }

    public static Season byIndex(int index) {
        Season[] enumArray = values();
        if (index >= 0 && index < enumArray.length) {
            return enumArray[index];
        } else {
            throw new IndexOutOfBoundsException("Invalid index: " + index);
        }
    }
}
