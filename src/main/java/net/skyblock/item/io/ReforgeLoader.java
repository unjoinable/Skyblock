package net.skyblock.item.io;

import net.skyblock.item.definition.Reforge;
import net.skyblock.item.enums.Rarity;
import net.skyblock.stats.calculator.StatProfile;
import net.skyblock.stats.definition.StatValueType;
import net.skyblock.stats.definition.Statistic;
import org.jetbrains.annotations.NotNull;
import org.tinylog.Logger;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Loads {@link Reforge}s from YAML configuration file {@code /reforges.yml}.
 * This class parses reforge definitions, handling potential errors by logging them
 * and skipping problematic entries rather than halting the entire process.
 */
public class ReforgeLoader {
    private static final String REFORGE_FILE_PATH = "/reforges.yml";
    private static final String STATS_KEY = "stats";

    /**
     * Loads all reforges from the YAML configuration file in resources.
     *
     * @return A list of valid {@link Reforge} objects parsed from the configuration.
     */
    public List<Reforge> loadReforges() {
        try (InputStream inputStream = getResourceStream();
             InputStreamReader reader = new InputStreamReader(inputStream)) {

            return parseYamlContent(reader);
        } catch (IOException e) {
            Logger.error(e, "Failed to read reforges file: {}. No reforges loaded.", REFORGE_FILE_PATH);
            return Collections.emptyList();
        } catch (Exception e) {
            Logger.error(e, "Unexpected error loading reforges from {}. No reforges loaded.", REFORGE_FILE_PATH);
            return Collections.emptyList();
        }
    }

    /**
     * Parses the YAML content from the reader.
     *
     * @param reader The reader containing YAML content
     * @return A list of parsed Reforge objects
     */
    private List<Reforge> parseYamlContent(InputStreamReader reader) {
        try {
            Object loadedYaml = new Yaml().load(reader);

            if (!(loadedYaml instanceof Map)) {
                Logger.error("Expected map structure in {}, but found {}. No reforges loaded.",
                        REFORGE_FILE_PATH,
                        loadedYaml != null ? loadedYaml.getClass().getSimpleName() : "null");
                return Collections.emptyList();
            }

            @SuppressWarnings("unchecked")
            Map<String, Object> reforgesData = (Map<String, Object>) loadedYaml;
            return parseReforgesFromMap(reforgesData);

        } catch (YAMLException e) {
            Logger.error(e, "Failed to parse YAML structure from {}. No reforges loaded.", REFORGE_FILE_PATH);
            return Collections.emptyList();
        }
    }

    /**
     * Gets the input stream for the reforge configuration file.
     *
     * @return An input stream for reading the reforge configuration.
     * @throws IllegalStateException if the file cannot be found.
     */
    private InputStream getResourceStream() {
        InputStream stream = getClass().getResourceAsStream(REFORGE_FILE_PATH);
        if (stream == null) {
            throw new IllegalStateException("Reforges file not found: " + REFORGE_FILE_PATH);
        }
        return stream;
    }

    /**
     * Parses individual reforges from the raw map data.
     *
     * @param reforgesData The raw map loaded from YAML.
     * @return A list of successfully parsed Reforge objects.
     */
    private List<Reforge> parseReforgesFromMap(@NotNull Map<String, Object> reforgesData) {
        List<Reforge> reforges = new ArrayList<>();
        int parsedCount = 0;

        for (Map.Entry<String, Object> entry : reforgesData.entrySet()) {
            String reforgeId = entry.getKey();
            Object value = entry.getValue();

            if (!(value instanceof Map)) {
                Logger.warn("Skipping reforge '{}': Expected map data, but got {}",
                        reforgeId,
                        value != null ? value.getClass().getSimpleName() : "null");
                continue;
            }

            try {
                @SuppressWarnings("unchecked")
                Map<String, Object> reforgeData = (Map<String, Object>) value;
                Map<Rarity, StatProfile> statsByRarity = parseReforgeStats(reforgeId, reforgeData);

                reforges.add(new Reforge(reforgeId, statsByRarity));
                parsedCount++;
            } catch (Exception e) {
                Logger.error(e, "Skipping reforge '{}' due to error during parsing", reforgeId);
            }
        }

        Logger.info("Loaded {} valid reforges out of {} total entries", parsedCount, reforgesData.size());
        return reforges;
    }

    /**
     * Parses the rarity-specific stats for a single reforge.
     *
     * @param reforgeId   The ID of the reforge being processed
     * @param reforgeData The map data for the specific reforge
     * @return A map of Rarity to its StatProfile
     */
    private Map<Rarity, StatProfile> parseReforgeStats(@NotNull String reforgeId, @NotNull Map<String, Object> reforgeData) {
        Map<Rarity, StatProfile> statsByRarity = new EnumMap<>(Rarity.class);
        Object statsValue = reforgeData.get(STATS_KEY);

        if (statsValue == null) {
            return statsByRarity; // Empty stats is valid
        }

        if (!(statsValue instanceof Map)) {
            Logger.warn("Invalid '{}' structure for reforge '{}': Expected map, but got {}",
                    STATS_KEY, reforgeId, statsValue.getClass().getSimpleName());
            return statsByRarity;
        }

        @SuppressWarnings("unchecked")
        Map<String, Object> statsData = (Map<String, Object>) statsValue;

        for (Map.Entry<String, Object> rarityEntry : statsData.entrySet()) {
            String rarityKey = rarityEntry.getKey();
            Object rarityValue = rarityEntry.getValue();

            try {
                parseRarityStats(reforgeId, rarityKey, rarityValue, statsByRarity);
            } catch (Exception e) {
                Logger.error(e, "Error processing rarity '{}' for reforge '{}'", rarityKey, reforgeId);
            }
        }

        return statsByRarity;
    }

    /**
     * Parses stats for a specific rarity of a reforge.
     *
     * @param reforgeId The ID of the reforge being processed
     * @param rarityKey The string key of the rarity
     * @param rarityValue The value associated with the rarity key
     * @param statsByRarity The map to populate with parsed results
     */
    private void parseRarityStats(
            String reforgeId,
            String rarityKey,
            Object rarityValue,
            Map<Rarity, StatProfile> statsByRarity) {

        Rarity rarity;
        try {
            rarity = Rarity.valueOf(rarityKey.toUpperCase());
        } catch (IllegalArgumentException _) {
            Logger.warn("Skipping invalid rarity '{}' for reforge '{}'", rarityKey, reforgeId);
            return;
        }

        if (!(rarityValue instanceof Map)) {
            Logger.warn("Skipping rarity '{}' for reforge '{}': Expected map data, but got {}",
                    rarityKey, reforgeId,
                    rarityValue != null ? rarityValue.getClass().getSimpleName() : "null");
            return;
        }

        @SuppressWarnings("unchecked")
        Map<String, Object> rarityStatsMap = (Map<String, Object>) rarityValue;
        StatProfile profile = createStatProfile(reforgeId, rarityKey, rarityStatsMap);
        statsByRarity.put(rarity, profile);
    }

    /**
     * Creates a StatProfile from a map of stat names to numeric values.
     *
     * @param reforgeId The ID of the reforge being processed
     * @param rarityKey The rarity key being processed
     * @param statsMap  The map containing stat names and values
     * @return The created StatProfile
     */
    private StatProfile createStatProfile(
            @NotNull String reforgeId,
            @NotNull String rarityKey,
            @NotNull Map<String, Object> statsMap) {

        StatProfile profile = new StatProfile();

        statsMap.forEach((statName, value) -> {
            if (!(value instanceof Number)) {
                Logger.warn("Skipping stat '{}' for reforge '{}', rarity '{}': Expected number, but got {}",
                        statName, reforgeId, rarityKey,
                        value != null ? value.getClass().getSimpleName() : "null");
                return;
            }

            try {
                Statistic stat = Statistic.valueOf(statName.toUpperCase());
                profile.addStat(stat, StatValueType.BASE, ((Number) value).doubleValue());
            } catch (IllegalArgumentException _) {
                Logger.warn("Skipping invalid statistic '{}' for reforge '{}', rarity '{}'",
                        statName, reforgeId, rarityKey);
            }
        });

        return profile;
    }
}