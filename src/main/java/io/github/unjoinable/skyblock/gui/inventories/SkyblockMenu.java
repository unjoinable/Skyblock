package io.github.unjoinable.skyblock.gui.inventories;

import com.google.common.collect.ImmutableSet;
import io.github.unjoinable.skyblock.gui.ClickableItem;
import io.github.unjoinable.skyblock.gui.SkyblockInventory;
import io.github.unjoinable.skyblock.statistics.Statistic;
import io.github.unjoinable.skyblock.user.SkyblockPlayer;
import io.github.unjoinable.skyblock.util.Utils;
import net.kyori.adventure.text.Component;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemComponent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.component.HeadProfile;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static io.github.unjoinable.skyblock.statistics.Statistic.*;
import static net.kyori.adventure.text.format.NamedTextColor.*;
import static net.kyori.adventure.text.format.TextDecoration.ITALIC;

public class SkyblockMenu extends SkyblockInventory {
    //stat display order for menu
    private static final Set<Statistic> displayedStats = ImmutableSet.of(
            HEALTH, DEFENSE, STRENGTH, SPEED, CRIT_CHANCE, CRIT_DAMAGE, INTELLIGENCE, MINING_SPEED, BONUS_ATTACK_SPEED,
            SEA_CREATURE_CHANCE, MAGIC_FIND, PET_LUCK, TRUE_DEFENSE, FEROCITY, ABILITY_DAMAGE, MINING_FORTUNE, FARMING_FORTUNE,
            FORAGING_FORTUNE, PRISTINE);


    private static final Component YOUR_SKYBLOCK_PROFILE = Component.text("Your Skyblock Profile", GREEN).decoration(ITALIC, false);
    private static final Component EQUIP_STATS = Component.text("View your equipment, stats,", GRAY).decoration(ITALIC, false);
    private static final Component AND_MORE = Component.text("and more!", GRAY).decoration(ITALIC, false);
    private static final Component CLICK_TO_VIEW = Component.text("Click to view", YELLOW).decoration(ITALIC, false);

    public SkyblockMenu(SkyblockPlayer player) {
        super(InventoryType.CHEST_6_ROW, Component.text("Skyblock Menu"));

        fill(createFillerItem(Material.GRAY_STAINED_GLASS_PANE));
        addClickableItem(ClickableItem.CLOSE_BUTTON, player, 49);
        setItemStack(13, getStatsHead(player));
    }

    public @NotNull ItemStack getStatsHead(SkyblockPlayer player) {
        PlayerSkin skin = player.getSkin();
        HeadProfile profile = new HeadProfile(skin);
        ItemStack.Builder builder = ItemStack.of(Material.PLAYER_HEAD).builder();
        Map<Statistic, Double> statistics = player.getStatsHandler().getOverallStats();

        List<Component> lore = new ArrayList<>();
        lore.add(EQUIP_STATS);
        lore.add(AND_MORE);
        lore.add(Component.empty());

        for (Statistic stat : displayedStats) {
            double value = statistics.get(stat);
            Component line = Component.text(stat.getSymbol() + " " + stat.getDisplayName() + " ", stat.getDisplayColor())
                    .append(Component.text(Utils.getDecimalFormat().format(value), WHITE))
                    .decoration(ITALIC, false);
            lore.add(line);
        }
        lore.add(Component.empty());
        lore.add(CLICK_TO_VIEW);

        builder.set(ItemComponent.CUSTOM_NAME, YOUR_SKYBLOCK_PROFILE);
        builder.set(ItemComponent.PROFILE, profile);
        builder.set(ItemComponent.LORE, lore);
        return builder.build();
    }
}
