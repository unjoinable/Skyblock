package net.unjoinable.skyblock.time;

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
    LATE_WINTER("Late Winter");

    private final String displayName;

    Season(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }

    public static Season fromOrdinal(int ordinal) {
        Season[] values = Season.values();
        if (ordinal < 0 || ordinal >= values.length) {
            throw new IndexOutOfBoundsException("Invalid ordinal for Season: " + ordinal);
        }
        return values[ordinal];
    }
}

