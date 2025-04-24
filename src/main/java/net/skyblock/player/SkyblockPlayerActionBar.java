package net.skyblock.player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.skyblock.stats.Statistic;
import net.skyblock.ui.actionbar.ActionBar;
import net.skyblock.ui.actionbar.ActionBarDisplay;
import net.skyblock.ui.actionbar.ActionBarSection;

import java.text.DecimalFormat;

/**
 * Manages the action bar display for Skyblock players.
 * <p>
 * This class handles updating and displaying health, defense, and mana information
 * in the player's action bar, with support for temporary replacements.
 */
public class SkyblockPlayerActionBar {
    private final SkyblockPlayer player;
    private final ActionBar actionBar;
    private final DecimalFormat statsFormat;

    private static final NamedTextColor HEALTH_COLOR = NamedTextColor.RED;
    private static final NamedTextColor DEFENSE_COLOR = NamedTextColor.GREEN;
    private static final NamedTextColor MANA_COLOR = NamedTextColor.AQUA;

    // Unicode symbols for stats
    private static final String HEALTH_SYMBOL = Statistic.HEALTH.getSymbol();
    private static final String DEFENSE_SYMBOL = Statistic.DEFENSE.getSymbol();
    private static final String MANA_SYMBOL = Statistic.INTELLIGENCE.getSymbol();

    /**
     * Creates a new action bar manager for the specified player.
     *
     * @param player The Skyblock player to manage the action bar for
     */
    public SkyblockPlayerActionBar(SkyblockPlayer player) {
        this.player = player;
        this.actionBar = new ActionBar();
        this.statsFormat = new DecimalFormat("#");

        // Initialize with default displays
        updateDefaultDisplays();
    }

    /**
     * Updates the default displays for each section of the action bar.
     * <p>
     * This method formats health, defense, and mana information according to
     * the current player statistics.
     */
    public void updateDefaultDisplays() {
        updateHealthDisplay();
        updateDefenseDisplay();
        updateManaDisplay();
    }

    /**
     * Updates the health section of the action bar.
     */
    public void updateHealthDisplay() {
        float currentHealth = player.getCurrentHealth();
        float maxHealth = player.getMaxHealth();

        Component healthDisplay = Component.text(
                statsFormat.format(currentHealth) + "/" +
                        statsFormat.format(maxHealth) + HEALTH_SYMBOL,
                HEALTH_COLOR
        );

        actionBar.setDefaultDisplay(ActionBarSection.HEALTH, healthDisplay);
    }

    /**
     * Updates the defense section of the action bar.
     */
    public void updateDefenseDisplay() {
        float defense = player.getStatProfile().get(Statistic.DEFENSE);

        Component defenseDisplay = Component.text(
                statsFormat.format(defense) + DEFENSE_SYMBOL + " Defense",
                DEFENSE_COLOR
        );

        actionBar.setDefaultDisplay(ActionBarSection.DEFENSE, defenseDisplay);
    }

    /**
     * Updates the mana section of the action bar.
     */
    public void updateManaDisplay() {
        float currentMana = player.getCurrentMana();
        float maxMana = player.getMaxMana();

        Component manaDisplay = Component.text(
                statsFormat.format(currentMana) + "/" +
                        statsFormat.format(maxMana) + MANA_SYMBOL + " Mana",
                MANA_COLOR
        );

        actionBar.setDefaultDisplay(ActionBarSection.MANA, manaDisplay);
    }

    /**
     * Adds a temporary replacement to a specific section of the action bar.
     * <p>
     * This is useful for ability notifications, collection updates, etc.
     *
     * @param section The section to replace temporarily
     * @param replacement The replacement display information
     */
    public void addReplacement(ActionBarSection section, ActionBarDisplay replacement) {
        actionBar.addReplacement(section, replacement);
    }

    /**
     * Builds and sends the current action bar to the player.
     */
    public void sendActionBar() {
        Component builtActionBar = actionBar.build();
        player.sendActionBar(builtActionBar);
    }

    /**
     * Updates the action bar
     * Prefer 5 tick in between
     */
    public void update() {
        updateDefaultDisplays();
        sendActionBar();
    }

    /**
     * Gets the underlying ActionBar object for direct manipulation.
     *
     * @return The ActionBar instance
     */
    public ActionBar getActionBar() {
        return actionBar;
    }
}
