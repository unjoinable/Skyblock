package io.github.unjoinable.skyblock.user.actionbar;

import io.github.unjoinable.skyblock.util.StringUtils;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.TaskSchedule;

import java.util.*;

/**
 * @author Swofty
 * @since 1.0.0
 */
public class ActionBar {
    private final Map<ActionBarSection, List<ActionBarDisplayReplacement>> replacements;
    private final Map<ActionBarSection, Component> defaultDisplays;

    /**
     * @since 1.0.0
     */
    public ActionBar() {
        this.replacements = new EnumMap<>(ActionBarSection.class);
        this. defaultDisplays = new EnumMap<>(ActionBarSection.class);
        ActionBarSection.getValues().forEach(section -> replacements.put(section, new ArrayList<>()));
    }

    /**
     * @param section The part of action bar for which we provided default display
     * @param display If there are no active replacement modifiers, then this display is going to be sent
     * @since 1.0.0
     */
    public void setDefaultDisplay(ActionBarSection section, Component display) {
        defaultDisplays.put(section, display);
    }

    /**
     * If the priority of new replacement is less, it's not going to get updated.
     * @param section The part of action bar which is going to get potentially updated.
     * @param replacement The replacement which potentially is going to get added.
     * @since 1.0.0
     */
    public void addReplacement(ActionBarSection section, ActionBarDisplayReplacement replacement) {
        List<ActionBarDisplayReplacement> sectionReplacements = replacements.get(section);
        sectionReplacements.add(replacement);
        sectionReplacements.sort(Comparator.comparingInt(ActionBarDisplayReplacement::priority).reversed());

        if (replacement.duration() > 0) {
            scheduleRemoval(section, replacement);
        }
    }

    /**
     * @param section The part of action bar scheduled for reset/removal.
     * @param replacement The replacement which is going to get removed.
     * @since 1.0.0
     */
    private void scheduleRemoval(ActionBarSection section, ActionBarDisplayReplacement replacement) {
        MinecraftServer.getSchedulerManager().scheduleTask(() ->
                replacements.get(section).remove(replacement),
                TaskSchedule.tick(replacement.duration()),
                TaskSchedule.stop());
    }

    /**
     * @return The ActionBar's content built while taking priorities into account.
     * @since 1.0.0
     */
    public Component build() {
        Component content = Component.empty();
        for (ActionBarSection section : ActionBarSection.getValues()) {
            Component display = getDisplayForSection(section);
            if (display != Component.empty()) {
                if (content != Component.empty()) {
                    content = content.append(StringUtils.toComponent("     "));
                }
                content = content.append(display);
            }
        }
        return content;
    }

    /**
     * @param section The part of action bar to obtain
     * @return The content of the section
     */
    private Component getDisplayForSection(ActionBarSection section) {
        List<ActionBarDisplayReplacement> sectionReplacements = replacements.get(section);
        if (!sectionReplacements.isEmpty()) {
            return sectionReplacements.getFirst().display();
        }
        return defaultDisplays.getOrDefault(section, Component.empty());
    }

}
