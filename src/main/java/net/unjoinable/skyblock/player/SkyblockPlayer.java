package net.unjoinable.skyblock.player;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.attribute.Attribute;
import net.minestom.server.timer.TaskSchedule;
import net.unjoinable.skyblock.level.Island;
import net.unjoinable.skyblock.level.SkyblockIsland;
import net.unjoinable.skyblock.player.factory.PlayerCreationContext;
import net.unjoinable.skyblock.player.rank.PlayerRank;
import net.unjoinable.skyblock.player.systems.AbilitySystem;
import net.unjoinable.skyblock.player.systems.CombatSystem;
import net.unjoinable.skyblock.player.systems.EconomySystem;
import net.unjoinable.skyblock.player.systems.PlayerStatSystem;
import net.unjoinable.skyblock.player.ui.actionbar.PlayerActionBar;
import net.unjoinable.skyblock.player.ui.sidebar.PlayerSidebar;

import static net.unjoinable.skyblock.combat.statistic.Statistic.SPEED;

/**
 * Extended Player class for Skyblock gameplay with custom systems and UI components.
 * <p>
 * This class extends Minestom's Player to provide Skyblock-specific functionality
 * including stat systems, action bar management, and invulnerability handling.
 */
public class SkyblockPlayer extends Player {
    private final PlayerStatSystem statSystem;
    private final EconomySystem economySystem;
    private final AbilitySystem abilitySystem;
    private final CombatSystem combatSystem;
    private final PlayerActionBar actionBar;
    private final PlayerSidebar sidebar;

    private PlayerRank playerRank;
    private final Island island;

    public SkyblockPlayer(PlayerCreationContext ctx) {
        super(ctx.connection(), ctx.gameProfile());

        // Systems
        this.statSystem = new PlayerStatSystem(this, ctx.itemProcessor());
        this.economySystem = new EconomySystem();
        this.abilitySystem = new AbilitySystem(this, ctx.itemProcessor());
        this.combatSystem = new CombatSystem(this);

        // UI
        this.actionBar = new PlayerActionBar(this);
        this.sidebar = new PlayerSidebar(this, ctx.skyblockTime());

        // Attribute
        this.getAttribute(Attribute.MAX_HEALTH).setBaseValue(40);
        this.playerRank = PlayerRank.HYPIXEL_STAFF;
        this.island = SkyblockIsland.HUB;
    }

    /**
     * Initializes the Skyblock player after login.
     * <p>
     * This method sets up the action bar and starts the recurring game loop task
     * which periodically updates the player's UI and systems.
     * It should be called once during the player's configuration phase.
     */
    public void init() {
        this.statSystem.resetHealthAndMana();
        this.sidebar.send();
        MinecraftServer.getSchedulerManager().scheduleTask(
                this::gameLoop, TaskSchedule.immediate(), TaskSchedule.seconds(1));
    }

    /**
     * The recurring game loop for the Skyblock player.
     * <p>
     * This method is called on a fixed interval (every 2 seconds) and is responsible
     * for updating the action bar and any other periodic systems or visual feedback.
     */
    private void gameLoop() {
        this.actionBar.update();
        this.statSystem.regenerateHealth();
        this.statSystem.regenerateMana();
        this.sidebar.update();
        this.getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue(getStatSystem().getStat(SPEED)/1000);
    }

    @Override
    public boolean isInvulnerable() {
        return statSystem.isInvulnerable();
    }

    @Override
    public void kill() {
        super.kill();
        this.statSystem.resetHealthAndMana();
    }

    /**
     * Sets the player's rank.
     *
     * <p>This method updates the player's current rank, which affects their permissions,
     * chat prefix display, and other rank-based features throughout the server.</p>
     *
     * @param playerRank The new {@link PlayerRank} to assign to this player
     */
    public void setPlayerRank(PlayerRank playerRank) {
        this.playerRank = playerRank;
    }

    /**
     * Gets the player's current rank.
     *
     * <p>Returns the player's assigned rank, which determines their permissions,
     * chat display formatting, and access to rank-specific features.</p>
     *
     * @return The player's current {@link PlayerRank}
     */
    public PlayerRank getPlayerRank() {
        return playerRank;
    }

    /**
     * Gets the player's stat system for managing health, mana, and other statistics.
     * <p>
     * This is a convenience method that provides direct access to the stat system
     * without needing to go through the systems manager.
     *
     * @return the player's stat system
     * @throws NullPointerException if the systems manager has not been set
     */
    public PlayerStatSystem getStatSystem() {
        return statSystem;
    }

    /**
     * Gets the player's economy system instance.
     *
     * @return the economy system managing this player's coins and bits
     */
    public EconomySystem getEconomySystem() {
        return this.economySystem;
    }

    /**
     * Gets the player's ability system instance.
     *
     * @return the system responsible for managing this player's item abilities and their usage
     */
    public AbilitySystem getAbilitySystem() {
        return abilitySystem;
    }

    /**
     * Gets the player's combat system instance.
     *
     * @return the system responsible for managing this player's combat behavior, including attacks,
     *         damage calculations, and related mechanics
     */
    public CombatSystem getCombatSystem() {
        return combatSystem;
    }

    /**
     * Gets the player's action bar component for displaying custom UI information.
     * <p>
     * The action bar is used to show Skyblock-specific information such as health,
     * mana, defense, and other important stats in a user-friendly format.
     *
     * @return the player's action bar component
     */
    public PlayerActionBar getActionBar() {
        return actionBar;
    }

    /**
     * Gets the island where this player is currently located.
     *
     * @return the player's current island
     */
    public Island getIsland() {
        return island;
    }
}