package net.unjoinable.skyblock.player.ui.actionbar;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.unjoinable.skyblock.player.SkyblockPlayer;
import net.unjoinable.skyblock.player.systems.PlayerStatSystem;
import net.unjoinable.skyblock.combat.statistic.Statistic;
import net.unjoinable.skyblock.ui.actionbar.ActionBar;
import net.unjoinable.skyblock.ui.actionbar.ActionBarSection;

import java.text.DecimalFormat;

public class PlayerActionBar extends ActionBar {
    private final SkyblockPlayer player;
    private final PlayerStatSystem statSystem;

    private static final DecimalFormat statsFormat = new DecimalFormat("#");

    private static final NamedTextColor HEALTH_COLOR = NamedTextColor.RED;
    private static final NamedTextColor DEFENSE_COLOR = NamedTextColor.GREEN;
    private static final NamedTextColor MANA_COLOR = NamedTextColor.AQUA;

    private static final String HEALTH_SYMBOL = Statistic.HEALTH.symbol();
    private static final String DEFENSE_SYMBOL = Statistic.DEFENSE.symbol();
    private static final String MANA_SYMBOL = Statistic.INTELLIGENCE.symbol();


    public PlayerActionBar(SkyblockPlayer player) {
        this.player = player;
        this.statSystem = player.getStatSystem();
        updateDefaultDisplays(); // init the system
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
        double currentHealth = statSystem.getCurrentHealth();
        double maxHealth = statSystem.getMaxHealth();

        Component healthDisplay = Component.text(
                statsFormat.format(currentHealth) + "/" +
                        statsFormat.format(maxHealth) + HEALTH_SYMBOL,
                HEALTH_COLOR
        );

        setDefaultDisplay(ActionBarSection.HEALTH, healthDisplay);
    }

    /**
     * Updates the defense section of the action bar.
     */
    public void updateDefenseDisplay() {
        double defense = statSystem.getStat(Statistic.DEFENSE);

        Component defenseDisplay = Component.text(
                statsFormat.format(defense) + DEFENSE_SYMBOL + " Defense",
                DEFENSE_COLOR
        );

        setDefaultDisplay(ActionBarSection.DEFENSE, defenseDisplay);
    }

    /**
     * Updates the mana section of the action bar.
     */
    public void updateManaDisplay() {
        double currentMana = statSystem.getCurrentMana();
        double maxMana = statSystem.getIntelligence();

        Component manaDisplay = Component.text(
                statsFormat.format(currentMana) + "/" +
                        statsFormat.format(maxMana) + MANA_SYMBOL + " Mana",
                MANA_COLOR
        );

        setDefaultDisplay(ActionBarSection.MANA, manaDisplay);
    }

    /**
     * Builds and sends the current action bar to the player.
     */
    public void sendActionBar() {
        player.sendActionBar(build());
    }

    /**
     * Updates the action bar
     * Prefer 5 tick in between
     */
    public void update() {
        updateDefaultDisplays();
        sendActionBar();
    }
}
